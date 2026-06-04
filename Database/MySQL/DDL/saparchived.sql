
CREATE TABLE IF NOT EXISTS saparchived
(
    docid   INTEGER     UNSIGNED NOT NULL AUTO_INCREMENT,
    chosed  ENUM('_','-','X') NOT NULL DEFAULT '_',
    yuud    VARCHAR(36) NOT NULL,
    augbl   VARCHAR(10) DEFAULT NULL,
    belnr   VARCHAR(10) NOT NULL,
    blart   VARCHAR(2)  DEFAULT NULL,
    bstat   VARCHAR(2)  DEFAULT NULL,
    budat   VARCHAR(8)  DEFAULT NULL,
    bukrs   VARCHAR(4)  DEFAULT NULL,
    ibelnr  VARCHAR(12) NOT NULL,
    iblart  VARCHAR(10) DEFAULT NULL,
    isgtxt  VARCHAR(80) DEFAULT NULL,
    ktokk   VARCHAR(4)  NOT NULL,
    lifnr   VARCHAR(10) NOT NULL,
    sgtxt   VARCHAR(50) DEFAULT NULL,
    sstat   ENUM('M','N','S','U') NOT NULL,
    waers   VARCHAR(5)  NOT NULL,
    xblnr   VARCHAR(16) DEFAULT NULL,
    zlsch   VARCHAR(1)  DEFAULT NULL,
    dated   DATETIME NOT NULL,
    INDEX   chosedndx(chosed),
    INDEX   yuudndx(yuud),
    PRIMARY KEY(docid),
    FOREIGN KEY(yuud) REFERENCES cancellations(cfdiuuid) ON UPDATE CASCADE ON DELETE RESTRICT
)   ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;
