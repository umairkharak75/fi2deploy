declare
  l_exst number;
  l_version varchar(20) := '0.93';
begin
    select count(*)
    into l_exst
    from nastavitve;

    if l_exst = 0 then
        insert into nastavitve(id, verzija) values (NASTAVITVE_seq.NEXTVAL, l_version);
    else
        update nastavitve set verzija = l_version where rownum=1;
    end if;

    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PRILOGA' and COLUMN_NAME = 'DAT_IZDAJE' and NULLABLE='N';

    if (l_exst > 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PRILOGA MODIFY DAT_IZDAJE DATE null';
    end if;

    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PRILOGA' and COLUMN_NAME = 'DAT_VELJ' and NULLABLE='N';

    if (l_exst > 0)  then
        EXECUTE IMMEDIATE 'ALTER TABLE PRILOGA MODIFY DAT_VELJ DATE null';
    end if;

    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'NASTAVITVE' and COLUMN_NAME = 'APP_NAME';

    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE NASTAVITVE ADD APP_NAME varchar(20) null';
        EXECUTE IMMEDIATE 'UPDATE NASTAVITVE set APP_NAME = ''TEST'' WHERE ROWNUM = 1';
        EXECUTE IMMEDIATE 'ALTER TABLE NASTAVITVE MODIFY APP_NAME varchar(20) not null';
    end if;
end;
