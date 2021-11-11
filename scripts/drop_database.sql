--Shutdown immediate;

--startup restrict;

  
DECLARE
  v_user_exists NUMBER;
  user_name CONSTANT varchar2(20) := 'ESK';
BEGIN
  LOOP
    FOR c IN (SELECT s.sid, s.serial# FROM v$session s WHERE upper(s.username) = user_name)
    LOOP
      EXECUTE IMMEDIATE
        'alter system kill session ''' || c.sid || ',' || c.serial# || ''' IMMEDIATE';
    END LOOP;
    BEGIN
      EXECUTE IMMEDIATE 'drop user ' || user_name || ' cascade';
      EXCEPTION WHEN OTHERS THEN
      IF (SQLCODE = -1940) THEN
        NULL;
      ELSE
        RAISE;
      END IF;
    END;
    BEGIN
      SELECT COUNT(*) INTO v_user_exists FROM dba_users WHERE username = user_name;
      EXIT WHEN v_user_exists = 0;
    END;
  END LOOP;
END;
/
