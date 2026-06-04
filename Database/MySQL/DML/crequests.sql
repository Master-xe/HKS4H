
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE crequest_insert
(
    IN cmpny    INTEGER UNSIGNED,
    IN scode    INTEGER UNSIGNED,
    IN stype    VARCHAR(16),
    IN detail   VARCHAR(999),
    IN message  VARCHAR(256),
    OUT rowid   INTEGER UNSIGNED
)
BEGIN
    INSERT INTO crequests VALUES(0, cmpny, detail, message, scode, stype, now());
    SET rowid = (SELECT LAST_INSERT_ID());
END $$
DELIMITER ;
