
CREATE TABLE IF NOT EXISTS downloads
(
    verificationId  INTEGER UNSIGNED NOT NULL,
    unzipped        ENUM('N','X','Y') NOT NULL,
    execDate        DATETIME NOT NULL,
    package         TEXT NOT NULL,
    KEY verificationId(verificationId),
    KEY execDate(execDate),
    INDEX unzipndx(unzipped)
)   ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = UTF8MB4;

