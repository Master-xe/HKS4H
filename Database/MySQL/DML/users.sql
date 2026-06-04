
DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE user_auth
(
    IN usrname  VARCHAR(16),
    IN usrpswd  VARCHAR(99),
    OUT message VARCHAR(16)
)
BEGIN

    SET message := 'OK';
    SET @id := (SELECT usrid FROM users WHERE username = usrname);
    SET @lckd := (SELECT lockedown FROM users WHERE usrid = @id);
    SET @pswd := (SELECT hashpswd FROM users WHERE usrid = @id);
    SET @cntr := (SELECT attempts FROM users WHERE usrid = @id);

    IF( @id IS NOT NULL ) THEN
        IF( @lckd = 'N' ) THEN
            IF( @pswd = usrpswd ) THEN
                UPDATE users SET lastlogin = now() , attempts = 0 WHERE usrid = @id;
                SET @hpsw := (SELECT COUNT(1) FROM passwords WHERE userid = @id);
                SET @cpsw := (SELECT IF(@hpsw < 2, 'null', 'false'));
                SELECT usrid, email, rolename, username, fullname, lockedown, @cpsw AS lastlogin, registered FROM users INNER JOIN profiles ON roleid = rolid WHERE usrid = @id;
            ELSE
                IF( @cntr = 4 ) THEN
                    UPDATE users SET lockedown = 'Y' WHERE usrid = @id;
                    SET message := 'Account Locked';
                ELSE
                    UPDATE users SET attempts = (@cntr + 1) WHERE usrid = @id;
                    SET message := 'Invalid Password';
                END IF;
            END IF;
        ELSE
            SET message := 'Account Locked';
        END IF;
    ELSE
        SET message := 'Invalid Username';
    END IF;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE user_delete
(
    IN userid   INTEGER UNSIGNED
)
BEGIN

    SET @quantity := (SELECT COUNT(1) FROM cancellations WHERE usernameid = userid);

    IF( @quantity = 0 ) THEN
        DELETE FROM passwords WHERE passwords.userid = userid;
    END IF;

    DELETE FROM users WHERE usrid = userid;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE user_insert
(
    IN rolid    TINYINT UNSIGNED,
    IN email    VARCHAR(64),
    IN username VARCHAR(16),
    IN fullname VARCHAR(64),
    IN hashpswd VARCHAR(99)
)
BEGIN

    INSERT INTO users VALUES(0, rolid, email, username, fullname, hashpswd, 0, 'N', null, now());

    SET @id = (SELECT LAST_INSERT_ID());
    INSERT INTO passwords VALUES(0, @id, hashpswd);

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE user_update
(
    IN usrid    INTEGER UNSIGNED,
    IN upswd    VARCHAR(99),
    IN email    VARCHAR(64),
    IN ulock    VARCHAR(1)
)
BEGIN

    SET @fieldset := CONCAT('lockedown = \'', ulock, '\',');

    IF( email IS NOT NULL ) THEN
        SET @fieldset := CONCAT(@fieldset, 'email = \'', email, '\',');
    END IF;

    IF( upswd IS NOT NULL ) THEN
        INSERT INTO passwords VALUES(0, usrid, upswd);
        SET @fieldset := CONCAT(@fieldset, 'hashpswd = \'', upswd, '\',');
    END IF;

    IF( @fieldset IS NOT NULL ) THEN
        SET @fieldset := CONCAT(@fieldset, 'attempts = 0');
        SET @statement := CONCAT('UPDATE users SET ', @fieldset, ' WHERE usrid = ', usrid);
        PREPARE query FROM @statement;
        EXECUTE query;
        DEALLOCATE PREPARE query;
    END IF;

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE users_view
(
    IN usrname  VARCHAR(16),
    IN lckdown  BOOLEAN
)
BEGIN

    SELECT
        usrid,
        email,
        rolename,
        username,
        fullname,
        lockedown,
        lastlogin,
        registered
    FROM
        users
    INNER JOIN
        profiles ON roleid = rolid
    WHERE
        usrname IS NULL OR username = usrname
    AND
        lckdown IS NULL OR IF(lckdown, lockedown = 'Y', lockedown = 'N');

END $$
DELIMITER ;

DELIMITER $$
CREATE DEFINER = 'portaluser'@'localhost' PROCEDURE password_reused
(
    IN hpaswrd  VARCHAR(99),
    IN userxid  INTEGER UNSIGNED,
    OUT result  INTEGER UNSIGNED
)
BEGIN

    DECLARE finished INTEGER DEFAULT FALSE;
    DECLARE thishash VARCHAR(99) DEFAULT NULL;
    DECLARE record CURSOR FOR SELECT pswdhash FROM passwords WHERE userid = userxid ORDER BY pswid LIMIT 12;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = TRUE;
    OPEN record; SET result = 0;

    read_loop: LOOP
        FETCH record INTO thishash;

        IF finished THEN
            LEAVE read_loop;
        END IF;

        IF thishash = hpaswrd THEN
            SET result = 1;
        END IF;

    END LOOP;

    CLOSE record;

END $$
DELIMITER ;
