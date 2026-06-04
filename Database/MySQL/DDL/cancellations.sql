
CREATE TABLE IF NOT EXISTS cancellations
(
    document    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
    requestp    INTEGER UNSIGNED NOT NULL,
    cfdiuuid    VARCHAR(36) NOT NULL,
    curstage    ENUM('INI','PAC','SAT','SAP','USR') NOT NULL DEFAULT 'INI',
    response    ENUM('Aceptacion','Rechazo','Pendiente','Manual') NOT NULL DEFAULT 'Pendiente',
    receipt     ENUM('N','X','Y') NOT NULL DEFAULT 'N',
    updated     DATETIME DEFAULT CURRENT_TIMESTAMP,
    elapsed     TINYINT  UNSIGNED DEFAULT 1,
    ruleid      SMALLINT NOT NULL DEFAULT 0,
    status      INTEGER  NOT NULL DEFAULT 0,
    usernameid  INTEGER  UNSIGNED DEFAULT 1,
    INDEX   cfdindx(cfdiuuid),
    INDEX   statusndx(status),
    INDEX   receiptndx(receipt),
    INDEX   requestndx(requestp),
    INDEX   responsendx(response),
    PRIMARY KEY(document),
    FOREIGN KEY(requestp) REFERENCES crequests(request) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY(usernameid) REFERENCES users(usrid)
)   ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;
