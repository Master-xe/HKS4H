
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE cancellation_insert
(
    reqid   INTEGER UNSIGNED,
    uuid    VARCHAR(36)
)
BEGIN

    SELECT @lastid := MAX(requestp) FROM cancellations WHERE cfdiuuid = uuid;

    IF( @lastid IS NULL ) THEN
        INSERT INTO cancellations(requestp, cfdiuuid) VALUES(reqid, uuid);
    ELSE
        SELECT @replied := receipt, @estatus := status FROM cancellations WHERE requestp = @lastid AND cfdiuuid = uuid;
        IF( @replied = 'Y' ) THEN
            INSERT INTO cancellations(requestp, cfdiuuid, curstage, status) VALUES(reqid, uuid, 'USR', @estatus);
        END IF;
    END IF;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE cancellations_view
(
    IN company  INTEGER UNSIGNED,
    IN userxid  INTEGER UNSIGNED,
    IN docuuid  VARCHAR(36),
    IN replied  BOOLEAN,
    IN startdt  DATE,
    IN enddate  DATE
)
BEGIN

    SELECT
        document,
        cfdiuuid,
        curstage,
        response,
        receipt,
        elapsed,
        updated,
        created,
        username,
        IF( ISNULL(koneshdata.docDate) OR (LENGTH(TRIM(koneshdata.docDate)) = 0), metadata.issueDate, koneshdata.docDate) AS issueDate,
        IF( ISNULL(koneshdata.docType) OR (LENGTH(TRIM(koneshdata.docType)) = 0), metadata.cfditype, koneshdata.docType) AS cfditype,
        IF( ISNULL(koneshdata.emiRFC) OR (LENGTH(TRIM(koneshdata.emiRFC)) = 0), metadata.rfcEmitter, koneshdata.emiRFC) AS rfcEmitter,
        IF( ISNULL(koneshdata.currency) OR (LENGTH(TRIM(koneshdata.currency)) = 0), metadata.currency, koneshdata.currency) AS currency,
        IF( ISNULL(koneshdata.folio) OR (LENGTH(TRIM(koneshdata.folio)) = 0), metadata.folio, koneshdata.folio) AS folio,
        IF( ISNULL(koneshdata.serie) OR (LENGTH(TRIM(koneshdata.serie)) = 0), metadata.serie, koneshdata.serie) AS serie,
        IF( koneshdata.total > 0, koneshdata.total, metadata.amount ) AS amount,
        koneshdata.message AS kmessage,
        koneshdata.status AS kstatus,
        companies.crfc AS rfcReceiver,
        orderNumber,
        finalResult,
        procType,
        seal,
        augbl,
        belnr,
        blart,
        bstat,
        budat,
        bukrs,
        ktokk,
        lifnr,
        sgtxt,
        sstat,
        xblnr,
        zlsch,
        ibelnr,
        iblart,
        isgtxt
    FROM
        cancellations
    INNER JOIN
        crequests ON request = requestp
    INNER JOIN
        companies ON cid = cmpnyid
    INNER JOIN
        users ON usrid = usernameid
    LEFT JOIN
        koneshdata ON koneshdata.uuid = cfdiuuid
    LEFT JOIN
        metadata ON metadata.uuid = cfdiuuid
    LEFT JOIN
        sapdocuments ON yuud = cfdiuuid
    WHERE
        IF( docuuid IS NOT NULL, cfdiuuid = docuuid, (company = 0 OR cmpnyid = company) AND (userxid = 0 OR usernameid = userxid) AND created BETWEEN startdt AND enddate)
    AND
        IF(replied, response IN('Aceptacion','Rechazo'), response IN('Manual','Pendiente'));

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE cancellations_viewsap()
BEGIN

    SELECT
        cfdiuuid,
        IF( ISNULL(koneshdata.docDate) OR (LENGTH(TRIM(koneshdata.docDate)) = 0), metadata.issueDate, koneshdata.docDate) AS issueDate,
        IF( ISNULL(koneshdata.docType) OR (LENGTH(TRIM(koneshdata.docType)) = 0), metadata.cfditype, koneshdata.docType) AS cfditype,
        IF( ISNULL(koneshdata.emiRFC) OR (LENGTH(TRIM(koneshdata.emiRFC)) = 0), metadata.rfcEmitter, koneshdata.emiRFC) AS rfcEmitter,
        IF( ISNULL(koneshdata.recRFC) OR (LENGTH(TRIM(koneshdata.recRFC)) = 0), metadata.rfcReceiver, koneshdata.recRFC) AS rfcReceiver,
        IF( ISNULL(koneshdata.folio) OR (LENGTH(TRIM(koneshdata.folio)) = 0), metadata.folio, koneshdata.folio) AS folio,
        IF( ISNULL(koneshdata.serie) OR (LENGTH(TRIM(koneshdata.serie)) = 0), metadata.serie, koneshdata.serie) AS serie,
        IF( koneshdata.total > 0, koneshdata.total, metadata.amount ) AS amount
    FROM
        cancellations
    LEFT JOIN
        koneshdata ON koneshdata.uuid = cfdiuuid
    LEFT JOIN
        metadata ON metadata.uuid = cfdiuuid
    WHERE
        response = 'Pendiente'
    AND
        curstage = 'PAC'
    AND
        cancellations.status IN(1,12);

END $$
DELIMITER ;

