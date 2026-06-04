
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE metadata_insert
(
    IN download     INTEGER UNSIGNED,
    IN estatus      TINYINT UNSIGNED,
    IN uuid         VARCHAR(36),
    IN emitter      VARCHAR(99),
    IN receiver     VARCHAR(99),
    IN rfcEmitter   VARCHAR(13),
    IN rfcReceiver  VARCHAR(13),
    IN satCertDate  DATETIME,
    IN issueDate    DATETIME,
    IN amount       DECIMAL(19,2),
    IN seal         VARCHAR(8),
    IN serie        VARCHAR(25),
    IN folio        VARCHAR(40),
    IN currency     VARCHAR(5),
    IN cfditype     VARCHAR(1),
    IN cancelled    DATETIME
)
BEGIN

    SET @flag := (SELECT IF(uuid = 'X', 'X', 'Y'));

    IF @flag = 'Y' THEN
        INSERT INTO
            metadata
        VALUES
            (uuid, emitter, receiver, rfcEmitter, rfcReceiver, satCertDate, issueDate, amount, seal, serie, folio, currency, cfditype, estatus, cancelled, now());
    END IF;

    UPDATE downloads SET unzipped = @flag WHERE verificationId = download;

    IF estatus = 1 THEN
        UPDATE cancellations SET status = 12 WHERE cfdiuuid = uuid;
    END IF;

END $$
DELIMITER ;
