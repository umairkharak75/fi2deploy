create or replace view evReport as
select 
    c.id_imetnik as id_subjekt, c.id_zasciten_proizvod, EXTRACT(YEAR from c.dat_izdaje) as leto, zp.id_shema
from 
    "CERTIFIKAT" c
    inner join "ZASCITENPROIZVOD" zp on c.id_zasciten_proizvod = zp.id
where 
    c.status in ('Veljaven', 'Neveljaven', 'Arhiv')
group by 
    c.id_imetnik, c.id_zasciten_proizvod, EXTRACT(YEAR from c.dat_izdaje), zp.id_shema
/

create or replace view ESK_DATA.evReportWithMebers as
select a.id_subjekt, a.id_zasciten_proizvod, a.leto, a.id_shema
from (
    select 
        c.id_imetnik as id_subjekt, c.id_zasciten_proizvod, EXTRACT(YEAR from c.dat_izdaje) as leto, zp.id_shema
    from 
        ESK_DATA.CERTIFIKAT c
        inner join ESK_DATA.ZASCITENPROIZVOD zp on c.id_zasciten_proizvod = zp.id
    where 
        c.status in ('Veljaven', 'Neveljaven', 'Arhiv')
    union
    select 
        p.id_subjekt, c.id_zasciten_proizvod, EXTRACT(YEAR from c.dat_izdaje) as leto, zp.id_shema
    from 
        ESK_DATA.CERTIFIKAT c
        inner join ESK_DATA.ZASCITENPROIZVOD zp on c.id_zasciten_proizvod = zp.id
        inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cp on c.id = cp.id_certifikat
        inner join ESK_DATA.PRILOGA_CLAN p on cp.id_priloga = p.id_priloga
    where 
        c.status in ('Veljaven', 'Neveljaven', 'Arhiv')
)a 
group by 
    a.id_subjekt, a.id_zasciten_proizvod, a.leto, a.id_shema
/

declare
  l_exst number;
BEGIN
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_DEJAVNOST' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_DEJAVNOST ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_EXT' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_EXT ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_PRILOGA_CLAN' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_PRILOGA_CLAN ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_PRILOGA_PROIZVOD' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_PRILOGA_PROIZVOD ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_PROIZVOD' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_PROIZVOD ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'DEJAVNOST' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE DEJAVNOST ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PRILOGA' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PRILOGA ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PRILOGA_CLAN' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PRILOGA_CLAN ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PROIZVOD' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PROIZVOD ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PROIZVODKOLICINE' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PROIZVODKOLICINE ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'SHEMA' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE SHEMA ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'SUBJEKT' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE SUBJEKT ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZAKONSKAPODLAGA' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZAKONSKAPODLAGA ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZASCITNIZNAK' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZASCITNIZNAK ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZASCITNIZNAK_PROIZVOD' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZASCITNIZNAK_PROIZVOD ADD SPREMENIL NUMBER null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZASCITENPROIZVOD' and COLUMN_NAME = 'SPREMENIL';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZASCITENPROIZVOD ADD SPREMENIL NUMBER null';
    end if;
END;
/


declare
  l_exst number;
BEGIN
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_DEJAVNOST' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_DEJAVNOST ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_EXT' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_EXT ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_PRILOGA_CLAN' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_PRILOGA_CLAN ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_PRILOGA_PROIZVOD' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_PRILOGA_PROIZVOD ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_PROIZVOD' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_PROIZVOD ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'DEJAVNOST' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE DEJAVNOST ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PRILOGA' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PRILOGA ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PRILOGA_CLAN' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PRILOGA_CLAN ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PROIZVOD' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PROIZVOD ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'PROIZVODKOLICINE' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE PROIZVODKOLICINE ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'SHEMA' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE SHEMA ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'SUBJEKT' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE SUBJEKT ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZAKONSKAPODLAGA' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZAKONSKAPODLAGA ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZASCITNIZNAK' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZASCITNIZNAK ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZASCITNIZNAK_PROIZVOD' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZASCITNIZNAK_PROIZVOD ADD DAT_SPREMEMBE DATE null';
    end if;
    
    SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'ZASCITENPROIZVOD' and COLUMN_NAME = 'DAT_SPREMEMBE';
    
    if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE ZASCITENPROIZVOD ADD DAT_SPREMEMBE DATE null';
    end if;
	
	SELECT count(*) into l_exst FROM USER_TAB_COLUMNS WHERE table_name = 'CERTIFIKAT_EXT' and COLUMN_NAME = 'PDF';
	if (l_exst = 0) then
        EXECUTE IMMEDIATE 'ALTER TABLE CERTIFIKAT_EXT ADD PDF CLOB null';
    end if;
END;
/