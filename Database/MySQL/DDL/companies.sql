
CREATE TABLE IF NOT EXISTS companies
(
    cid     INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
    crfc    VARCHAR(13) NOT NULL UNIQUE,
    ccode   VARCHAR(4) NOT NULL UNIQUE,
    clock   ENUM('N','Y') DEFAULT 'N',
    cname   VARCHAR(99) NOT NULL,
    cpswd   VARCHAR(99) NOT NULL,
    cfile   BLOB NOT NULL,
    csdfl   BLOB NOT NULL,
    cdate   DATETIME NOT NULL,
    cupdt   DATETIME NOT NULL,
    expires DATE DEFAULT NULL,
    reqtype ENUM('CFDI','Metadata') NOT NULL DEFAULT 'CFDI',
    INDEX   companyndx(clock,crfc),
    PRIMARY KEY(cid)
)  ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

