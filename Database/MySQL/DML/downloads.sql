
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE unzip_downloads()
BEGIN

    SELECT verificationId, package FROM downloads WHERE unzipped = 'N';

END $$
DELIMITER ;

