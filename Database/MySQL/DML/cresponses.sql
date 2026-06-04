
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE rrequest_insert
(
    IN cmpny    INTEGER UNSIGNED,
    IN docid    INTEGER UNSIGNED,
    IN scode    INTEGER UNSIGNED,
    IN stype    VARCHAR(16),
    IN detail   VARCHAR(999),
    IN message  VARCHAR(256)
)
BEGIN

    SET @receipt_ := (SELECT IF(scode = 1000, 'Y', 'X'));
    UPDATE cancellations SET receipt = @receipt_ WHERE document = docid;
    INSERT INTO cresponses VALUES(docid, cmpny, detail, message, scode, stype, now());

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE respond_list
(
    IN company  INTEGER UNSIGNED
)
BEGIN

    SELECT
        document, cfdiuuid, response
    FROM
        cancellations
    INNER JOIN
        crequests ON request = requestp
    WHERE
        cmpnyid = company
    AND
        receipt IN('N','X')
    AND
        response IN('Aceptacion','Rechazo');

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE respond_request
(
    IN docid    INTEGER UNSIGNED,
    IN usrid    INTEGER UNSIGNED,
    IN reply    VARCHAR(10)
)
BEGIN
    UPDATE cancellations SET response = reply, status = 15, curstage = 'USR', usernameid = usrid, updated = now() WHERE document = docid;
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE respond_requests()
BEGIN

    DECLARE finished INTEGER DEFAULT FALSE;
    DECLARE docid, hours INTEGER DEFAULT 0;
    DECLARE record CURSOR FOR SELECT document, elapsed FROM cancellations WHERE response IN('Manual','Pendiente');
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = TRUE;

    OPEN record;

    read_loop: LOOP
        FETCH record INTO docid, hours;

        IF finished THEN
            LEAVE read_loop;
        END IF;

        SET hours = hours + 1;

        IF hours > 70 THEN
            UPDATE cancellations SET response = 'Rechazo', status = 99, usernameid = 1, updated = now() WHERE document = docid;
        ELSE
            UPDATE cancellations SET elapsed = hours WHERE document = docid;
        END IF;
    END LOOP;

    CLOSE record;

END $$
DELIMITER ;

