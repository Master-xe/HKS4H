
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE setting_insert
(
    IN ilabel   VARCHAR(16),
    IN ivalue   VARCHAR(1024),
    IN iunit    VARCHAR(16),
    IN idesc    VARCHAR(128)
)
BEGIN
    INSERT INTO settings VALUES(ilabel, ivalue, iunit, idesc);
END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE settings_view
(
    IN parameter    VARCHAR(16)
)
BEGIN

    SELECT
        slabel,
        svalue,
        sunit,
        sdesc
    FROM
        settings
    WHERE
        parameter IS NULL OR slabel = parameter;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE catalogs
(
    IN itable   VARCHAR(16)
)
BEGIN

    IF itable = 'companies' THEN
        SELECT CONCAT(ccode, ' - ', cname) AS label, cid AS entry FROM companies;
    ELSEIF itable = 'users' THEN
        SELECT usrid AS entry, username AS label FROM users;
    ELSEIF itable = 'profiles' THEN
        SELECT roleid AS entry, rolename AS label FROM profiles WHERE roleid > 1;
    END IF;

END $$
DELIMITER ;

