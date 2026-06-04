
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE company_insert
(
    IN rfc  VARCHAR(13),
    IN name VARCHAR(99),
    IN pswd VARCHAR(99),
    IN data VARCHAR(8),
    IN code VARCHAR(4),
    IN stat VARCHAR(1),
    IN fcsd BLOB,
    IN fpfx BLOB
)
BEGIN
    INSERT INTO companies VALUES(0, rfc, code, stat, name, pswd, fpfx, fcsd, now(), now(), NULL, data);
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE company_delete
(
    IN company  INTEGER UNSIGNED
)
BEGIN
    DELETE FROM companies WHERE cid = company;
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE company_update
(
    IN xcid INTEGER UNSIGNED,
    IN pswd VARCHAR(99),
    IN stat VARCHAR(1),
    IN fcsd BLOB,
    IN fpfx BLOB
)
BEGIN

    IF( pswd IS NOT NULL ) THEN
        UPDATE companies SET cpswd = pswd WHERE cid = xcid;
    END IF;

    IF( fpfx IS NOT NULL ) THEN
        UPDATE companies SET cfile = fpfx WHERE cid = xcid;
    END IF;

    IF( fcsd IS NOT NULL ) THEN
        UPDATE companies SET csdfl = fcsd WHERE cid = xcid;
    END IF;

    UPDATE companies SET clock = stat, cupdt = now() WHERE cid = xcid;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE companies_view
(
    IN lck  VARCHAR(1),
    IN rfc  VARCHAR(13)
)
BEGIN

    SELECT
        cid,
        crfc,
        ccode,
        cdate,
        csdfl,
        cfile,
        clock,
        cname,
        cpswd,
        cupdt,
        reqtype
    FROM
        companies
    WHERE
        lck IS NULL OR clock = lck
    AND
        rfc IS NULL OR crfc = rfc;

END $$
DELIMITER ;
