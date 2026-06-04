
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE sapdocument_insert
(
    IN yuud     VARCHAR(36),
    IN augbl    VARCHAR(10),
    IN belnr    VARCHAR(10),
    IN blart    VARCHAR(2),
    IN bstat    VARCHAR(2),
    IN budat    VARCHAR(8),
    IN bukrs    VARCHAR(4),
    IN ibelnr   VARCHAR(12),
    IN iblart   VARCHAR(10),
    IN isgtxt   VARCHAR(80),
    IN ktokk    VARCHAR(4),
    IN lifnr    VARCHAR(10),
    IN sgtxt    VARCHAR(50),
    IN sstat    VARCHAR(1),
    IN waers    VARCHAR(5),
    IN xblnr    VARCHAR(16),
    IN zlsch    VARCHAR(1)
)
BEGIN

    SET @xbelnr := (SELECT IF(belnr IS NULL, '_', belnr));
    SET @xktokk := (SELECT IF(ktokk IS NULL, '_', ktokk));
    SET @xlifnr := (SELECT IF(lifnr IS NULL, '0', lifnr));
    SET @xwaers := (SELECT IF(waers IS NULL, 'XXX', waers));
    SET @xibelnr := (SELECT IF(ibelnr IS NULL, '_', ibelnr));

    IF sstat = 'M' THEN
        INSERT INTO
            saparchived
        VALUES
            (0, '_', yuud, augbl, @xbelnr, blart, bstat, budat, bukrs, @xibelnr, iblart, isgtxt, @xktokk, @xlifnr, sgtxt, sstat, @xwaers, xblnr, zlsch, now());
    ELSE
        INSERT INTO
            sapdocuments
        VALUES
            (yuud, augbl, @xbelnr, blart, bstat, budat, bukrs, @xibelnr, iblart, isgtxt, @xktokk, @xlifnr, sgtxt, sstat, @xwaers, xblnr, zlsch, now());

        SET @estatus = (SELECT status FROM cancellations WHERE cfdiuuid = yuud AND response = 'Pendiente');

        IF @estatus = 1 THEN

            SET @result := fn_KoneshAndSAPEval(yuud, sstat, bstat, augbl);

            IF @result = 1 THEN
                UPDATE cancellations SET response = 'Aceptacion' WHERE cfdiuuid = yuud AND response = 'Pendiente';
            ELSEIF @result = 2 THEN
                UPDATE cancellations SET response = 'Rechazo' WHERE cfdiuuid = yuud AND response = 'Pendiente';
            ELSE
                UPDATE cancellations SET response = 'Manual' WHERE cfdiuuid = yuud AND response = 'Pendiente';
            END IF;

        ELSEIF @estatus = 12 THEN

            SET @result := fn_SAPEval(sstat, bstat, augbl);

            IF @result = 1 THEN
                UPDATE cancellations SET response = 'Aceptacion' WHERE cfdiuuid = yuud AND response = 'Pendiente';
            ELSEIF @result = 2 THEN
                UPDATE cancellations SET response = 'Rechazo' WHERE cfdiuuid = yuud AND response = 'Pendiente';
            ELSE
                UPDATE cancellations SET response = 'Manual' WHERE cfdiuuid = yuud AND response = 'Pendiente';
            END IF;

        END IF;
    END IF;

    UPDATE
        cancellations
    SET
        curstage = 'SAP', status = 14, updated = now()
    WHERE
        cfdiuuid = yuud;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE saparchived_select
(
    IN document INTEGER UNSIGNED,
    IN uuid     VARCHAR(36)
)
BEGIN

    UPDATE
        saparchived
    SET
        chosed = '-'
    WHERE
        yuud = uuid;

    UPDATE
        saparchived
    SET
        chosed = 'X'
    WHERE
        docid = document;

    INSERT INTO
        sapdocuments(yuud, augbl, belnr, blart, bstat, budat, bukrs, ibelnr, iblart, isgtxt, ktokk, lifnr, sgtxt, sstat, waers, xblnr, zlsch, dated)
    SELECT
        yuud, augbl, belnr, blart, bstat, budat, bukrs, ibelnr, iblart, isgtxt, ktokk, lifnr, sgtxt, sstat, waers, xblnr, zlsch, now()
    FROM
        saparchived
    WHERE
        docid = document;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE saparchived_view()
BEGIN

    SELECT
        docid, yuud, augbl, belnr, blart, bstat, budat, bukrs, ibelnr, iblart, isgtxt, ktokk, lifnr, sgtxt, waers, xblnr, zlsch
    FROM
        saparchived
    WHERE
        chosed = '_'
    ORDER BY
        yuud;

END $$
DELIMITER ;

