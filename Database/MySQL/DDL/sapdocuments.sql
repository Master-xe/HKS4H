
CREATE TABLE IF NOT EXISTS sapdocuments
(
    yuud    VARCHAR(36) NOT NULL UNIQUE, -- sgtxt
    augbl   VARCHAR(10) DEFAULT NULL,
    belnr   VARCHAR(10) NOT NULL,
    blart   VARCHAR(2)  DEFAULT NULL,
    bstat   VARCHAR(2)  DEFAULT NULL, -- stsim
    budat   VARCHAR(8)  DEFAULT NULL,
    bukrs   VARCHAR(4)  DEFAULT NULL,
    ibelnr  VARCHAR(12) NOT NULL, -- docim
    iblart  VARCHAR(10) DEFAULT NULL, -- clsim
    isgtxt  VARCHAR(80) DEFAULT NULL, -- dscim
    ktokk   VARCHAR(4)  NOT NULL,
    lifnr   VARCHAR(10) NOT NULL,
    sgtxt   VARCHAR(50) DEFAULT NULL, -- txtps
    sstat   ENUM('M','N','S','U') NOT NULL, -- bstat
    waers   VARCHAR(5)  NOT NULL,
    xblnr   VARCHAR(16) DEFAULT NULL,
    zlsch   VARCHAR(1)  DEFAULT NULL,
    dated   DATETIME NOT NULL,
    INDEX   imndx(ibelnr),
    INDEX   findx(belnr),
    PRIMARY KEY(yuud),
    FOREIGN KEY(yuud) REFERENCES cancellations(cfdiuuid) ON UPDATE CASCADE ON DELETE RESTRICT
)   ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

