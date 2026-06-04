
CREATE TABLE IF NOT EXISTS crones
(
    cron            VARCHAR(36) NOT NULL,
    executionType   VARCHAR(1) DEFAULT NULL,
    value           VARCHAR(8) DEFAULT NULL,
    PRIMARY KEY (cron)
)   ENGINE = InnoDB DEFAULT CHARSET = UTF8MB4;
