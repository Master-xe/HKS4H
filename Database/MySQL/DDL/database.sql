-- SHOW CREATE PROCEDURE db.procedure\G
CREATE DATABASE IF NOT EXISTS portalcfdi; -- SHOW PROCEDURE STATUS WHERE DB = 'database';
CREATE USER IF NOT EXISTS 'portaluser'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'portaluser';

GRANT DELETE ON portalcfdi.* TO 'portaluser'@'localhost';
GRANT INSERT ON portalcfdi.* TO 'portaluser'@'localhost';
GRANT SELECT ON portalcfdi.* TO 'portaluser'@'localhost';
GRANT UPDATE ON portalcfdi.* TO 'portaluser'@'localhost';
GRANT EXECUTE ON portalcfdi.* TO 'portaluser'@'localhost';
GRANT CREATE TEMPORARY TABLES ON portalcfdi.* TO 'portaluser'@'localhost';

FLUSH PRIVILEGES;
