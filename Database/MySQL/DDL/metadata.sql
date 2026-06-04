
CREATE TABLE IF NOT EXISTS metadata
(
    uuid        VARCHAR(36) NOT NULL UNIQUE,
    emitter     VARCHAR(99) DEFAULT NULL,
    receiver    VARCHAR(99) DEFAULT NULL,
    rfcEmitter  VARCHAR(13) NOT NULL,
    rfcReceiver VARCHAR(13) NOT NULL,
    satCertDate DATETIME DEFAULT NULL,
    issueDate   DATETIME DEFAULT NULL,
    amount      DECIMAL(19,2) NOT NULL,
    seal        VARCHAR(8) DEFAULT NULL,
    serie       VARCHAR(25) DEFAULT NULL,
    folio       VARCHAR(40) DEFAULT NULL,
    currency    VARCHAR(5) NOT NULL,
    cfditype    VARCHAR(1) NOT NULL,
    estatus     TINYINT UNSIGNED NOT NULL,
    cancelled   DATETIME DEFAULT NULL,
    recorded    DATETIME NOT NULL,
    INDEX   rfcendx(rfcEmitter),
    PRIMARY KEY(uuid)
)   ENGINE = InnoDB DEFAULT CHARSET = UTF8MB4;
