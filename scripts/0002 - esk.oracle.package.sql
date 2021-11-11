create or replace PACKAGE esk_mgmt AS 
    PROCEDURE login (
        username VARCHAR2, 
        pass VARCHAR2, 
        USER_ID OUT number
    );

    PROCEDURE get_profile_data (
        USERNAME APP_ADMIN.ESK_USERS.USER_NAME%type,
        ORG_SIF OUT APP_ADMIN.ESK_USERS.ORG_SIF%type,
        ORG_IME OUT APP_ADMIN.ESK_USERS.ORG_IME%type,
        ORG_NASLOV OUT APP_ADMIN.ESK_USERS.ORG_NASLOV%type,
        USER_ID OUT APP_ADMIN.ESK_USERS.USER_ID%type,
        DELAVEC_IME OUT APP_ADMIN.ESK_USERS.DELAVEC_IME%type,
        MAIL OUT APP_ADMIN.ESK_USERS.MAIL%type,
        TEL OUT APP_ADMIN.ESK_USERS.TEL%type,
        PRAVICE OUT VARCHAR2
    );
	
    FUNCTION get_permissions (id_user NUMBER) return VARCHAR2;

    PROCEDURE has_permission(id_user NUMBER, permission_name varchar2, result out BOOLEAN);

    PROCEDURE deleteUnusedProizvodData;

    PROCEDURE deleteCertificateProducts(id_cert NUMBER, v_id_user number);

    PROCEDURE deleteCertificateActivities(id_cert NUMBER, v_id_user number);

    PROCEDURE deleteTrademarkProducts(id_zznak NUMBER);

    PROCEDURE takeoverData(id_cert NUMBER, v_id_user NUMBER);

    PROCEDURE getPravnaPodlaga(id_cert NUMBER, zpresult out number);

    PROCEDURE getCertificateList
    (
        v_id Certifikat.id%type default null, -- 1
        v_stevilka Certifikat.stevilka%type default null, -- 2
        v_dat_izdaje varchar2 default null, -- 3
        v_dat_kontrole varchar2 default null, -- 4
        v_dat_velj varchar2 default null,  -- 5
        v_dat_vnosa varchar2 default null, --6
        v_dat_izdaje_do varchar2 default null, -- 7
        v_dat_kontrole_do varchar2 default null, -- 8
        v_dat_velj_do varchar2 default null, -- 9
        v_dat_vnosa_do varchar2 default null, --10
        v_kontrolor certifikat.kontrolor%type default null, -- 11
        v_id_uporabnik certifikat.id_uporabnik%type default null, -- 12
        v_statusi varchar2 default null, -- 13
        v_dej_naziv Dejavnost.naziv%type default null, -- 14
        v_zp_naziv ZascitenProizvod.naziv%type default null, -- 15
        v_proizvod varchar2 default null, -- 16
        v_shema_naziv shema.naziv%type default null, -- 17
        v_kmgmid subjekt.kmgmid%type default null, -- 18
        v_sub_naziv Subjekt.naziv%type default null, -- 19
        v_sub_davcna subjekt.davcna%type default null, --20
        v_sub_maticna Subjekt.maticna%type default null, -- 21
        v_sub_id_poste Subjekt.id_poste%type default null, -- 22
        v_sub_posta Subjekt.posta%type default null, -- 23
        v_sub_obcina Subjekt.obcina%type default null, -- 24
        v_organizacija varchar2 default null, -- 25
        my_cursor OUT SYS_REFCURSOR
    );

    PROCEDURE getPrilogaClanList
    (
        v_query varchar2 default null,
        v_organizacija varchar2 default null, -- 25
        my_cursor OUT SYS_REFCURSOR
    );

    PROCEDURE getPrilogaProizvodList
    (
        v_query varchar2 default null,
        v_organizacija varchar2 default null, -- 25
        my_cursor OUT SYS_REFCURSOR
    );

    PROCEDURE updateCertificateAttachments(id_cert NUMBER, v_id_user number);

    PROCEDURE invalidateCertificates;

    PROCEDURE saveCertificateAttachment(v_id_cert NUMBER, v_id_priloga NUMBER, v_id OUT number);

    PROCEDURE saveAttachmentMember(v_id_subj NUMBER, v_id_priloga NUMBER, v_id OUT number);

    PROCEDURE saveProduct(v_id_cert NUMBER, v_id_product NUMBER, v_id_user NUMBER, v_id OUT number);

    PROCEDURE saveActivity(v_id_cert NUMBER, v_id_activity NUMBER, v_id_user NUMBER, v_id OUT number);

    PROCEDURE getPrevAttachment(id_cert in NUMBER, ptype in NUMBER, id_cert_priloga out NUMBER);

    PROCEDURE deleteAttachmentMembers( v_id_priloga NUMBER, v_id_user NUMBER);

    PROCEDURE getReportData(v_report in varchar2, v_year in number, v_mytable out SYS_REFCURSOR);

    PROCEDURE getKolicineList(v_query varchar2 default null, my_cursor OUT SYS_REFCURSOR);
END esk_mgmt;
/
CREATE OR REPLACE PACKAGE BODY ESK_DATA.esk_mgmt AS
    init BOOLEAN := FALSE;

    PROCEDURE login (
        username VARCHAR2,
        pass VARCHAR2,
        USER_ID OUT number
    )
    IS
    BEGIN
        USER_ID := APP_ADMIN.ESK_PKG.esk_login(username,pass);
    END login;

    PROCEDURE get_profile_data (
        USERNAME APP_ADMIN.ESK_USERS.USER_NAME%type,
        ORG_SIF OUT APP_ADMIN.ESK_USERS.ORG_SIF%type ,
        ORG_IME OUT APP_ADMIN.ESK_USERS.ORG_IME%type,
        ORG_NASLOV OUT APP_ADMIN.ESK_USERS.ORG_NASLOV%type,
        USER_ID OUT APP_ADMIN.ESK_USERS.USER_ID%type,
        DELAVEC_IME OUT APP_ADMIN.ESK_USERS.DELAVEC_IME%type,
        MAIL OUT APP_ADMIN.ESK_USERS.MAIL%type,
        TEL OUT APP_ADMIN.ESK_USERS.TEL%type,
        PRAVICE OUT VARCHAR2
    )
    IS
    BEGIN
        select
            NVL(ko.SIF, a.ORG_SIF) as ORG_SIF,
            a.ORG_IME,
            a.ORG_NASLOV,
            a.USER_ID,
            a.DELAVEC_IME,
            a.MAIL,
            a.TEL
        into
            ORG_SIF,
            ORG_IME,
            ORG_NASLOV,
            USER_ID,
            DELAVEC_IME,
            MAIL,
            TEL
        from
            APP_ADMIN.ESK_USERS a
			LEFT JOIN ESK_DATA.KO ko on a.ORG_SIF = ko.EXT_SIF
        where
            a.user_name = USERNAME;

        PRAVICE := get_permissions(user_id);

    END get_profile_data;

    FUNCTION get_permissions (id_user NUMBER) return VARCHAR2 IS
        v_id_user NUMBER;
        v_permissions nvarchar2(2000);
        v_permission nvarchar2(100);
        v_res char(1);
    BEGIN
        v_permissions := '';
        v_id_user := id_user;
        
		v_permission := 'CERTIFIKAT_PREG_MOJA_ORG';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_AZURIRAJ';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_IZBRIS';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_ZAKLJUCI';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_RAZVELJAVI';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_CLANI_IZBRISI';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_OSVEZI_PARTNERJE';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'DEJAVNOST_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'DEJAVNOST_AZURIRAJ';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'UVOZ_KOLICINE';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'UVOZ_ZZNAK';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'UVOZ_CLANI';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'UVOZ_PROIZVODI';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'UVOZ_CERTIFIKATI';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'PRILOGE_CLAN_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'PRILOGE_PROIZVOD_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'PROIZVOD_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'PROIZVOD_AZURIRAJ';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'KOLICINE_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'POIZVEDBA_PRIPRAVLJENA_POROCILA';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'ZAK_PODL_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'ZAK_PODL_AZURIRAJ';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'ZZNAK_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'ZZNAK_AZURIRAJ';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'SIFRANTI_PREGLED';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_PREG_VSE';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;
        v_permission := 'CERTIFIKAT_IZVOZ';
        v_res := APP_ADMIN.ESK_PKG.GET_USER_APP_PRIV(v_id_user, v_permission);
        if (v_res = 'D') then
            v_permissions := v_permissions || v_permission || ',';
        end if;

        return v_permissions;

    END get_permissions;

    PROCEDURE has_permission(id_user NUMBER, permission_name varchar2, result out BOOLEAN)
    is
    begin
        result:= true;
    END has_permission;

    PROCEDURE getPravnaPodlaga(id_cert NUMBER, zpresult out NUMBER)
    as
        res NUMBER;
        cnt NUMBER := 0;
    begin
        res := 0;
        -- CLEAN COPY PASTED DATA
        update ESK_DATA.zakonskapodlaga 
        set vsebina = 
            case when INSTR(vsebina, '<![endif]-->', -1) = 0 then vsebina
            else substr(vsebina, INSTR(vsebina, '<![endif]-->', -1)+12, length(vsebina))
            end
        where INSTR(vsebina, '<![endif]-->', -1) > 0;
        
        update ESK_DATA.zakonskapodlaga
        set vsebina = replace(vsebina, '<br></br><br></br><br></br><br></br><br></br><br></br><br></br>', '')
        where INSTR(vsebina, '<br></br><br></br><br></br><br></br><br></br><br></br><br></br>', -1) > 0;

        select count(a.id) into cnt
            from ESK_DATA.CERTIFIKAT a
            inner join ESK_DATA.PROIZVOD b on a.id_zasciten_proizvod = b.id_zasciten_proizvod
            inner join ESK_DATA.ZAKONSKAPODLAGA c on b.ID_ZAKONSKA_PODLAGA = c.id
            where a.id = id_cert and b.ID_ZAKONSKA_PODLAGA is not null and b.naziv is null;

         if (cnt > 0) then
            select c.id into res
            from ESK_DATA.CERTIFIKAT a
            inner join ESK_DATA.PROIZVOD b on a.id_zasciten_proizvod = b.id_zasciten_proizvod
            inner join ESK_DATA.ZAKONSKAPODLAGA c on b.ID_ZAKONSKA_PODLAGA = c.id
            where a.id = id_cert and b.ID_ZAKONSKA_PODLAGA is not null and b.naziv is null;
        else
            res := 0;
        end if;

        if (res = 0) then
            select count(id_certifikat) into cnt
                from ESK_DATA.CERTIFIKAT_PROIZVOD a
                inner join ESK_DATA.PROIZVOD b on a.id_proizvod = b.id
                inner join ESK_DATA.ZAKONSKAPODLAGA c on b.ID_ZAKONSKA_PODLAGA = c.id
                where a.id_certifikat = id_cert and b.ID_ZAKONSKA_PODLAGA is not null;

            if (cnt > 0) then
				select c.id into res
                    from ESK_DATA.CERTIFIKAT_PROIZVOD a
                    inner join ESK_DATA.PROIZVOD b on a.id_proizvod = b.id
                    inner join ESK_DATA.ZAKONSKAPODLAGA c on b.ID_ZAKONSKA_PODLAGA = c.id
                    where a.id_certifikat = id_cert and b.ID_ZAKONSKA_PODLAGA is not null
						and ROWNUM = 1;
            end if;
        end if;
        zpresult := res;
    END getPravnaPodlaga;

    PROCEDURE deleteUnusedProizvodData AS
    BEGIN
        UPDATE ESK_DATA.KO set naziv = naziv where 1 = 0; 
    END deleteUnusedProizvodData;

    PROCEDURE deleteCertificateProducts(id_cert number, v_id_user number) IS
    BEGIN
      DELETE FROM ESK_DATA.CERTIFIKAT_PROIZVOD WHERE id_certifikat = id_cert;
          UPDATE ESK_DATA.CERTIFIKAT_PROIZVOD_HIST SET spremenil = v_id_user WHERE id_certifikat = id_cert and akcija = 'D';
    END deleteCertificateProducts;

    PROCEDURE deleteCertificateActivities(id_cert IN number, v_id_user number) IS
    BEGIN
      DELETE FROM ESK_DATA.CERTIFIKAT_DEJAVNOST WHERE id_certifikat = id_cert;
          UPDATE ESK_DATA.CERTIFIKAT_DEJAVNOST_HIST SET spremenil = v_id_user WHERE id_certifikat = id_cert and akcija = 'D';
    END deleteCertificateActivities;

    PROCEDURE takeoverData(id_cert IN number, v_id_user NUMBER) IS
        cnt NUMBER := 0;
        v_proizvod VARCHAR2(250);
    BEGIN
       
        select lower(trim(zp.naziv)) into v_proizvod
        from ESK_DATA.certifikat c
        inner join ESK_DATA.ZASCITENPROIZVOD zp on c.id_zasciten_proizvod = zp.id
        where c.id = id_cert;
       
        if (v_proizvod = 'mleko') then
            insert into ESK_DATA.PROIZVODKOLICINE (id, vrednost, enota, leto, kmgmid, NAZIV_SUBJ, NASLOV, ID_POSTE, posta, DAVCNA, maticna, TEL_ST, email, shema, ZASCITENPROIZVOD, PROIZVOD, spremenil)
            select
                ESK_DATA.PROIZVODKOLICINE_seq.nextval, ml.NEPOSREDNA_ODDAJA_MLEKA + ml.ODDAJA_MLEKA + ml.PROIZVAJALCI_MLEKO_KOL, 'l' as enota, ml.leto, ml.KMG_MID, NVL(clan.NAZIV, clan.ime || ' ' || clan.priimek) as naziv, clan.NASLOV, clan.ID_POSTE, clan.POSTA, clan.DAVCNA, clan.maticna, clan.TEL_ST, clan.EMAIL, 'Izbrana kakovost' as shema,'mleko' as zp,'' as sk, v_id_user
            from
                ESK_DATA.CERTIFIKAT c
                inner join ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.ID
                inner join ESK_DATA.SHEMA sh on sh.id = zp.ID_SHEMA
                inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp ON cpp.id_certifikat = c.id
                inner join ESK_DATA.PRILOGA_CLAN pc ON cpp.id_priloga = pc.id_priloga
                inner join ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id
                inner join ESK_DATA.EV_MLEKO_G ml on clan.KMGMID = ml.KMG_MID and EXTRACT(year from c.DAT_IZDAJE)-1 = ml.LETO
                left join ESK_DATA.PROIZVODKOLICINE kolicine on
                    lower(kolicine.shema) = lower(sh.naziv) and
                    lower(kolicine.zascitenproizvod) = lower(zp.naziv) and
                    clan.kmgmid = kolicine.kmgmid and
                    kolicine.leto =  EXTRACT(year from c.DAT_IZDAJE)-2
            where
                c.id = id_cert and kolicine.id is null;
                
            insert into ESK_DATA.PROIZVODKOLICINE (id, vrednost, enota, leto, kmgmid, NAZIV_SUBJ, NASLOV, ID_POSTE, posta, DAVCNA, maticna, TEL_ST, email, shema, ZASCITENPROIZVOD, PROIZVOD, spremenil)
            select
                ESK_DATA.PROIZVODKOLICINE_seq.nextval, ml.NEPOSREDNA_ODDAJA_MLEKA + ml.ODDAJA_MLEKA + ml.PROIZVAJALCI_MLEKO_KOL, 'l' as enota, ml.leto, ml.KMG_MID, NVL(s.NAZIV, s.ime || ' ' || s.priimek) as naziv, s.NASLOV, s.ID_POSTE, s.POSTA, s.DAVCNA, s.maticna, s.TEL_ST, s.EMAIL, 'Izbrana kakovost' as shema,'mleko' as zp,'' as sk, v_id_user
            from
                ESK_DATA.CERTIFIKAT c
                inner join ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.ID
                inner join ESK_DATA.SHEMA sh on sh.id = zp.ID_SHEMA
                inner join ESK_DATA.SUBJEKT s on c.ID_IMETNIK = s.ID
                inner join ESK_DATA.EV_MLEKO_G ml on s.KMGMID = ml.KMG_MID and EXTRACT(year from c.DAT_IZDAJE)-1 = ml.LETO
                left join ESK_DATA.PROIZVODKOLICINE kolicine on
                    lower(kolicine.shema) = lower(sh.naziv) and
                    lower(kolicine.zascitenproizvod) = lower(zp.naziv) and
                    s.kmgmid = kolicine.kmgmid and
                    kolicine.leto =  EXTRACT(year from c.DAT_IZDAJE)-2
            where
                c.id = id_cert and kolicine.id is null;
       end if;
       
       if (v_proizvod = 'govedo') then
            insert into ESK_DATA.PROIZVODKOLICINE (id, vrednost, enota, leto, kmgmid, NAZIV_SUBJ, NASLOV, ID_POSTE, posta, DAVCNA, maticna, TEL_ST, email, shema, ZASCITENPROIZVOD, PROIZVOD, SPREMENIL)
            select
                ESK_DATA.PROIZVODKOLICINE_seq.nextval, go.teza, 'kg' as enota, go.leto, go.KMG_MID, NVL(s.NAZIV, s.ime || ' ' || s.priimek) as naziv, s.NASLOV, s.ID_POSTE, s.POSTA, s.DAVCNA, s.maticna, s.TEL_ST, s.EMAIL, 'Izbrana kakovost' as shema, 'govedo' as zp, '' as sk, v_id_user
            from
                ESK_DATA.CERTIFIKAT c
                inner join ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.ID
                inner join ESK_DATA.SHEMA sh on sh.id = zp.ID_SHEMA
                inner join ESK_DATA.SUBJEKT s on c.ID_IMETNIK = s.ID
                inner join ESK_DATA.ZAKOL_PO_KMG_MID_ESK_G go on s.KMGMID = go.KMG_MID and EXTRACT(year from c.DAT_IZDAJE)-1 = go.LETO
                left join ESK_DATA.PROIZVODKOLICINE kolicine on
                    lower(kolicine.shema) = lower(sh.naziv) and
                    lower(kolicine.zascitenproizvod) = lower(zp.naziv) and
                    s.kmgmid = kolicine.kmgmid and
                    kolicine.leto =  EXTRACT(year from c.DAT_IZDAJE)-1
            where
                c.id = id_cert and kolicine.id is null;
                
            insert into ESK_DATA.PROIZVODKOLICINE (id, vrednost, enota, leto, kmgmid, NAZIV_SUBJ, NASLOV, ID_POSTE, posta, DAVCNA, maticna, TEL_ST, email, shema, ZASCITENPROIZVOD, PROIZVOD, SPREMENIL)
            select
                ESK_DATA.PROIZVODKOLICINE_seq.nextval, go.teza, 'kg' as enota, go.leto, go.KMG_MID, NVL(clan.NAZIV, clan.ime || ' ' || clan.priimek) as naziv, clan.NASLOV, clan.ID_POSTE, clan.POSTA, clan.DAVCNA, clan.maticna, clan.TEL_ST, clan.EMAIL, 'Izbrana kakovost' as shema, 'govedo' as zp, '' as sk, v_id_user
            from
                ESK_DATA.CERTIFIKAT c
                inner join ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.ID
                inner join ESK_DATA.SHEMA sh on sh.id = zp.ID_SHEMA
                inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp ON cpp.id_certifikat = c.id
                inner join ESK_DATA.PRILOGA_CLAN pc ON cpp.id_priloga = pc.id_priloga
                inner join ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id
                inner join ESK_DATA.ZAKOL_PO_KMG_MID_ESK_G go on clan.KMGMID = go.KMG_MID and EXTRACT(year from c.DAT_IZDAJE)-1 = go.LETO
                left join ESK_DATA.PROIZVODKOLICINE kolicine on
                    lower(kolicine.shema) = lower(sh.naziv) and
                    lower(kolicine.zascitenproizvod) = lower(zp.naziv) and
                    clan.kmgmid = kolicine.kmgmid and
                    kolicine.leto =  EXTRACT(year from c.DAT_IZDAJE)-1
            where
                c.id = id_cert and kolicine.id is null;
        end if;

        if (v_proizvod = 'sadje') then
            -- INTEGRACJA SADJE
			-- IMETNIKI
            insert into ESK_DATA.PROIZVODKOLICINE (id, vrednost, enota, leto, kmgmid, NAZIV_SUBJ, NASLOV, ID_POSTE, posta, DAVCNA, maticna, TEL_ST, email, shema, ZASCITENPROIZVOD, PROIZVOD, SPREMENIL)
            select
                ESK_DATA.PROIZVODKOLICINE_seq.nextval, sad.SUM_POV_VRSTA/10000, 'ha' as enota, EXTRACT(year from c.DAT_IZDAJE), sad.KMG_MID, NVL(s.NAZIV, s.ime || ' ' || s.priimek) as naziv, s.NASLOV, s.ID_POSTE, s.POSTA, s.DAVCNA, s.maticna, s.TEL_ST, s.EMAIL, 'Izbrana kakovost' as shema, 'sadje' as zp, p.NAZIV as sk, v_id_user
            from
                ESK_DATA.CERTIFIKAT c
                inner join ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.ID
                inner join ESK_DATA.SHEMA sh on sh.id = zp.ID_SHEMA
                inner join ESK_DATA.CERTIFIKAT_PROIZVOD cp on cp.ID_CERTIFIKAT = c.ID
                inner join ESK_DATA.PROIZVOD p on cp.ID_PROIZVOD = p.ID
                inner join ESK_DATA.SUBJEKT s on c.ID_IMETNIK = s.ID
                inner join ESK_DATA.ESK_KMG_ISAD_AREA_G sad on s.KMGMID = sad.KMG_MID and lower(zp.naziv) = 'sadje' and lower(p.NAZIV) = lower(sad.NAZIV)
                left join ESK_DATA.PROIZVODKOLICINE kolicine on
                    lower(kolicine.shema) = lower(sh.naziv) and
                    lower(kolicine.zascitenproizvod) = lower(zp.naziv) and
                    lower(kolicine.proizvod) = lower(p.naziv) and
                    s.kmgmid = kolicine.kmgmid and
                    kolicine.leto = EXTRACT(year from c.DAT_IZDAJE)
            where
                c.id = id_cert and kolicine.id is null;
			
			insert into ESK_DATA.PROIZVODKOLICINE (id, vrednost, enota, leto, kmgmid, NAZIV_SUBJ, NASLOV, ID_POSTE, posta, DAVCNA, maticna, TEL_ST, email, shema, ZASCITENPROIZVOD, PROIZVOD, SPREMENIL)
            -- ČLANI
			select
                ESK_DATA.PROIZVODKOLICINE_seq.nextval, sad.SUM_POV_VRSTA/10000, 'ha' as enota, EXTRACT(year from c.DAT_IZDAJE), sad.KMG_MID, NVL(clan.NAZIV, clan.ime || ' ' || clan.priimek) as naziv, clan.NASLOV, clan.ID_POSTE, clan.POSTA, clan.DAVCNA, clan.maticna, clan.TEL_ST, clan.EMAIL, 'Izbrana kakovost' as shema, 'sadje' as zp, p.NAZIV as sk, v_id_user
            from
                ESK_DATA.CERTIFIKAT c
				inner join ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.ID
                inner join ESK_DATA.SHEMA sh on sh.id = zp.ID_SHEMA
                inner join ESK_DATA.CERTIFIKAT_PROIZVOD cp on cp.ID_CERTIFIKAT = c.ID
				inner join ESK_DATA.PROIZVOD p ON cp.id_proizvod = p.id
				inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp ON cpp.id_certifikat = c.id
				inner join ESK_DATA.PRILOGA_CLAN pc ON cpp.id_priloga = pc.id_priloga
				inner join ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id
				inner join ESK_DATA.ESK_KMG_ISAD_AREA_G sad on clan.id is not null and clan.KMGMID = sad.KMG_MID and lower(zp.naziv) = 'sadje' and lower(p.NAZIV) = lower(sad.NAZIV)
				left join ESK_DATA.PROIZVODKOLICINE kolicine on
                    lower(kolicine.shema) = lower(sh.naziv) and
                    lower(kolicine.zascitenproizvod) = lower(zp.naziv) and
                    lower(kolicine.proizvod) = lower(p.naziv) and
                    clan.kmgmid = kolicine.kmgmid and
                    kolicine.leto = EXTRACT(year from c.DAT_IZDAJE)
            where
                c.id = id_cert and kolicine.id is null;	
        end if;
    END takeoverData;

    PROCEDURE deleteTrademarkProducts(id_zznak IN number) IS
    BEGIN
      DELETE FROM ESK_DATA.ZASCITNIZNAK_PROIZVOD WHERE id_zascitniznak = id_zznak;
    END deleteTrademarkProducts;

    PROCEDURE getCertificateList
    (
        v_id Certifikat.id%type default null, -- 1
        v_stevilka Certifikat.stevilka%type default null, -- 2
        v_dat_izdaje varchar2 default null, -- 3
        v_dat_kontrole varchar2 default null, -- 4
        v_dat_velj varchar2 default null,  -- 5
        v_dat_vnosa varchar2 default null, --6
        v_dat_izdaje_do varchar2 default null, -- 7
        v_dat_kontrole_do varchar2 default null, -- 8
        v_dat_velj_do varchar2 default null, -- 9
        v_dat_vnosa_do varchar2 default null, --10
        v_kontrolor certifikat.kontrolor%type default null, -- 11
        v_id_uporabnik certifikat.id_uporabnik%type default null, -- 12
        v_statusi varchar2 default null, -- 13
        v_dej_naziv Dejavnost.naziv%type default null, -- 14
        v_zp_naziv ZascitenProizvod.naziv%type default null, -- 15
        v_proizvod varchar2 default null, -- 16
        v_shema_naziv shema.naziv%type default null, -- 17
        v_kmgmid subjekt.kmgmid%type default null, -- 18
        v_sub_naziv Subjekt.naziv%type default null, -- 19
        v_sub_davcna subjekt.davcna%type default null, --20
        v_sub_maticna Subjekt.maticna%type default null, -- 21
        v_sub_id_poste Subjekt.id_poste%type default null, -- 22
        v_sub_posta Subjekt.posta%type default null, -- 23
        v_sub_obcina Subjekt.obcina%type default null, -- 24
        v_organizacija varchar2 default null, -- 25
        my_cursor OUT SYS_REFCURSOR
    )
    IS
        v_base_query clob;
        v_where clob;
        v_where_by_subjekt clob;
        v_proizvodi_join clob;
    BEGIN
        v_base_query := '
        with binds as
        (
            select
                :bind1 as v_id,
                :bind2 as v_stevilka,
                :bind3 as v_dat_izdaje,
                :bind4 as v_dat_kontrole,
                :bind5 as v_dat_velj,
                :bind6 as v_dat_vnosa,
                :bind7 as v_dat_izdaje_do,
                :bind8 as v_dat_kontrole_do,
                :bind9 as v_dat_velj_do,
                :bind10 as v_dat_vnosa_do,
                :bind11 as v_kontrolor,
                :bind12 as v_id_uporabnik,
                :bind13 as v_statusi,
                :bind14 as v_dej_naziv,
                :bind15 as v_zp_naziv,
                :bind16 as v_proizvod,
                :bind17 as v_shema_naziv,
                :bind18 as v_kmgmid,
                :bind19 as v_sub_naziv,
                :bind20 as v_sub_davcna,
                :bind21 as v_sub_maticna,
                :bind22 as v_sub_id_poste,
                :bind23 as v_sub_posta,
                :bind24 as v_sub_obcina,
                :bind25 as v_organizacija
            from dual
        ) -- !!! BE CAREFULL TO ADD NEW RESULT COLUMN. THEN YOU NEED TO ADD PARSING IN ANGULAR ALSO!! ADDING IT TO THE END EVERY TIME!!!!!
        select
            c.id,
            c.TIP,
            c.STEVILKA,
            TO_CHAR(c.DAT_KONTROLE, ''YYYY-MM-DD'') as DAT_KONTROLE,
            TO_CHAR(c.DAT_IZDAJE, ''YYYY-MM-DD'') AS DAT_IZDAJE,
            TO_CHAR(c.DAT_VELJ, ''YYYY-MM-DD'') AS DAT_VELJ,
            c.STATUS,
            c.TEL_ST,
            c.EMAIL,
            c.opomba,
            TO_CHAR(c.DAT_VNOSA, ''YYYY-MM-DD'') AS DAT_VNOSA,
            c.KONTROLOR,
            d.id as dej_id,
            d.naziv as dej_naziv,
            z.id as zp_id,
            z.naziv as zp_naziv,
            s.id as shema_id,
            s.naziv as shema_naziv,
            sub.id as sub_id,
            sub.KMGMID as sub_KMGMID,
            sub.IME as sub_IME,
            sub.PRIIMEK as sub_PRIIMEK,
            sub.NAZIV as sub_NAZIV,
            sub.NASLOV as sub_NASLOV,
            sub.ID_POSTE as sub_ID_POSTE,
            sub.POSTA as sub_POSTA,
            sub.TEL_ST as sub_TEL_ST,
            sub.EMAIL as sub_EMAIL,
            sub.ID_SUBJ as sub_ID_SUBJ, -- KEY of subject
            sub.OB_ID as sub_OB_ID,
            sub.OBCINA as sub_OBCINA,
            sub.DAT_ZS as sub_DAT_ZS, -- KEY of subject
            c.id_uporabnik as uporabnik_id,
            c.organizacija as uporabnik_org,
            u.USER_NAME as uporabnik_username,
            u.DELAVEC_IME as uporabnik_ime,
            c.id_parent,
            c.organizacija
        from 
            ESK_DATA.CERTIFIKAT c
            INNER JOIN (
                select
                    c1.ID
                from
                    ESK_DATA.CERTIFIKAT c1
                    INNER JOIN ESK_DATA.SUBJEKT sub on c1.id_imetnik = sub.id
                    LEFT JOIN ESK_DATA.CERTIFIKAT_PROIZVOD cp ON c1.id = cp.id_certifikat
                    LEFT JOIN ESK_DATA.PROIZVOD p ON cp.id_proizvod = p.id
                    LEFT JOIN ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp ON cpp.id_certifikat = c1.id
                    LEFT JOIN ESK_DATA.PRILOGA_CLAN pc ON cpp.id_priloga = pc.id_priloga
                    LEFT JOIN ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id
                    , binds b1
                {{where_by_subjekt}}
                group by 
                    c1.ID) a on c.id = a.id
            INNER JOIN ESK_DATA.DEJAVNOST d on c.id_dejavnost = D.id
            INNER JOIN ESK_DATA.ZASCITENPROIZVOD z on c.id_zasciten_proizvod = z.id
            INNER JOIN ESK_DATA.SHEMA s on z.id_shema = s.id
            INNER JOIN ESK_DATA.SUBJEKT sub on c.id_imetnik = sub.id
            INNER JOIN APP_ADMIN.ESK_USERS u on c.id_uporabnik = u.user_id
            {{PROIZVODI}}, binds b
      where 1=1 ';

        v_proizvodi_join :=
               'INNER JOIN (
                    select
                        a.id_certifikat
                    from (
                        select
                            cp.id_certifikat, p.naziv
                        from ESK_DATA.CERTIFIKAT_PROIZVOD cp
                        inner join ESK_DATA.PROIZVOD p on cp.id_proizvod = p.id) a, binds b2
                    where b2.v_proizvod like ''%''||a.naziv||''%''
                    group by
                        a.id_certifikat
                ) proizvod on proizvod.id_certifikat = c.id';

        if v_proizvod is not null then
          v_base_query := REPLACE(v_base_query, '{{PROIZVODI}}', v_proizvodi_join);
        else
          v_base_query := REPLACE(v_base_query, '{{PROIZVODI}}', '');
        end if;
        
        if v_id is not null or v_id > 0 then
          v_where := v_where || ' and c.id = b.v_id';
        end if;

        if v_stevilka is not null then
          v_where := v_where || ' and lower(c.stevilka) = lower(b.v_stevilka)';
        end if;

        if v_dat_izdaje is not null or v_dat_izdaje_do is not null then
          v_where := v_where || ' and c.dat_izdaje >= to_date(NVL(b.v_dat_izdaje, ''19000101''),''YYYYMMDD'') and c.dat_izdaje <= NVL(to_date(b.v_dat_izdaje_do, ''YYYYMMDD''), sysdate + TO_YMINTERVAL ( ''10-0'' )) + 1';
        end if;

        if v_dat_kontrole is not null or v_dat_kontrole_do is not null then
          v_where := v_where || ' and c.dat_kontrole >= to_date(NVL(b.v_dat_kontrole, ''19000101''),''YYYYMMDD'') and c.dat_kontrole <= NVL(to_date(b.v_dat_kontrole_do, ''YYYYMMDD''), sysdate + TO_YMINTERVAL ( ''10-0'' )) + 1';
        end if;

        if v_dat_velj is not null or v_dat_velj_do is not null then
          v_where := v_where || ' and c.dat_velj >= to_date(NVL(b.v_dat_velj, ''19000101''),''YYYYMMDD'') and c.dat_velj <= NVL(to_date(b.v_dat_velj_do, ''YYYYMMDD''), sysdate + TO_YMINTERVAL ( ''10-0'' )) + 1';
        end if;

        if v_dat_vnosa is not null or v_dat_vnosa_do is not null then
          v_where := v_where || ' and c.dat_vnosa >= to_date(NVL(b.v_dat_vnosa, ''19000101''),''YYYYMMDD'') and c.dat_vnosa <= NVL(to_date(b.v_dat_vnosa_do, ''YYYYMMDD''), sysdate + TO_YMINTERVAL ( ''10-0'' )) + 1';
        end if;

        if v_kontrolor is not null or TRIM(v_kontrolor) != '' then
          v_where := v_where || ' and lower(c.kontrolor) like ''%''||lower(b.v_kontrolor)||''%'' ';
        end if;

        if v_statusi is not null then
          v_where := v_where || ' and lower(b.v_statusi) = lower(c.status) ';
        end if;

        if v_dej_naziv is not null then
          v_where := v_where || ' and lower(d.naziv) like ''%''||lower(b.v_dej_naziv)||''%'' ';
        end if;

        if v_zp_naziv is not null then
          v_where := v_where || ' and lower(z.naziv) like ''%''||lower(b.v_zp_naziv)||''%'' ';
        end if;
        
        if v_shema_naziv is not null then
          v_where := v_where || ' and lower(s.naziv) like ''%''||lower(b.v_shema_naziv)||''%'' ';
        end if;
        
        if v_organizacija is not null then
          v_where := v_where || ' and c.organizacija = upper(b.v_organizacija) ';
        end if;
        
        -- TODO clani
        if v_kmgmid is not null or TRIM(v_kmgmid) != '' then
          v_where_by_subjekt := v_where_by_subjekt || ' and (lower(sub.kmgmid) = lower(b1.v_kmgmid) or lower(clan.kmgmid) = lower(b1.v_kmgmid))';
        end if;

        if v_sub_naziv is not null then
          v_where_by_subjekt := v_where_by_subjekt || ' and (lower(sub.naziv) like ''%''||lower(b1.v_sub_naziv)||''%'' or lower(clan.naziv) like ''%''||lower(b1.v_sub_naziv)||''%'')';
        end if;

        if v_sub_davcna is not null then
          v_where_by_subjekt := v_where_by_subjekt || ' and (sub.davcna like ''''||b1.v_sub_davcna||''%'' or clan.davcna like ''''||b1.v_sub_davcna||''%'')';
        end if;

        if v_sub_maticna is not null then
          v_where_by_subjekt := v_where_by_subjekt || ' and (sub.maticna like ''''||b1.v_sub_maticna||''%'' or clan.maticna like ''''||b1.v_sub_maticna||''%'')';
        end if;

        if v_sub_posta is not null then
          v_where_by_subjekt := v_where_by_subjekt || ' and (lower(sub.posta) like ''%''||lower(b1.v_sub_posta)||''%'' or lower(clan.posta) like ''%''||lower(b1.v_sub_posta)||''%'')';
        end if;

        if v_sub_id_poste is not null or v_sub_id_poste > 0 then
          v_where_by_subjekt := v_where_by_subjekt || ' and (sub.id_poste like ''%''||b1.v_sub_id_poste||''%'' or clan.id_poste like ''%''||b1.v_sub_id_poste||''%'')';
        end if;

        if v_sub_obcina is not null then
          v_where_by_subjekt := v_where_by_subjekt || ' and (lower(sub.obcina) like ''%''||lower(b1.v_sub_obcina)||''%'' or lower(clan.obcina) like ''%''||lower(b1.v_sub_obcina)||''%'')';
        end if;

        if v_where_by_subjekt is not null then
          v_base_query := REPLACE(v_base_query, '{{where_by_subjekt}}', 'WHERE ' || substr(v_where_by_subjekt, 5));
        else
          v_base_query := REPLACE(v_base_query, '{{where_by_subjekt}}', '');
        end if;

        open my_cursor for v_base_query || v_where
        using
            v_id,
            v_stevilka,
            v_dat_izdaje,
            v_dat_kontrole,
            v_dat_velj,
            v_dat_vnosa,
            v_dat_izdaje_do,
            v_dat_kontrole_do,
            v_dat_velj_do,
            v_dat_vnosa_do,
            v_kontrolor,
            v_id_uporabnik,
            v_statusi,
            v_dej_naziv,
            v_zp_naziv,
            v_proizvod,
            v_shema_naziv,
            v_kmgmid,
            v_sub_naziv,
            v_sub_davcna,
            v_sub_maticna,
            v_sub_id_poste,
            v_sub_posta,
            v_sub_obcina,
            v_organizacija;
    END getCertificateList;

    PROCEDURE getPrilogaClanList
    (
        v_query varchar2 default null, -- 24
        v_organizacija varchar2 default null, -- 25
        my_cursor OUT SYS_REFCURSOR
    )
    IS
        v_base_query clob;
        v_where clob;
        v_proizvodi_join clob;
    BEGIN
        v_base_query := '
        with binds as
        (
            select
                :bind1 as v_query,
                :bind2 as v_organizacija
            from dual
        ) -- !!! BE CAREFULL TO ADD NEW RESULT COLUMN. THEN YOU NEED TO ADD PARSING IN ANGULAR ALSO!! ADDING IT TO THE END EVERY TIME!!!!!
        select
                        cpp.id_priloga as id,
                        sub.kmgmid as kmgmid_imetnika,
                        case when sub.naziv is null then sub.ime || '' '' || sub.priimek else sub.naziv end as naziv_imetnika,
            sub.naslov || '', '' || sub.id_poste || '' '' || sub.posta as naslov_imetnika,
                        c.stevilka as stevilka_certifikata,
                        sh.naziv as shema,
                        zp.naziv as naziv_zascita,
            clan.kmgmid as kmgmid_clana,
            case when clan.naziv is null then clan.ime || '' '' || clan.priimek else clan.naziv end as naziv_clana,
            clan.naslov || '', '' || clan.id_poste || '' '' || clan.posta as naslov_clana,
                        p.stevilka as stevilka,
                        TO_CHAR(p.DAT_IZDAJE, ''YYYY-MM-DD'') AS DAT_IZDAJE,
                        TO_CHAR(p.DAT_VELJ, ''YYYY-MM-DD'') AS DAT_VELJ,
                        p.status as status,
            c.id as id_certifikat,
            TO_CHAR(c.DAT_VNOSA, ''YYYY-MM-DD'') AS DAT_VNOSA
                from
                        ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp
            inner join ESK_DATA.PRILOGA p on cpp.id_priloga = p.id
            inner join ESK_DATA.PRILOGA_CLAN pc on p.id = pc.id_priloga
            inner join ESK_DATA.SUBJEKT clan on pc.id_subjekt = clan.id
            inner join ESK_DATA.CERTIFIKAT c on cpp.id_certifikat = c.id
            inner join ESK_DATA.SUBJEKT sub on c.id_imetnik = sub.id
            inner join ESK_DATA.ZASCITENPROIZVOD zp on c.id_zasciten_proizvod = zp.id
            inner join ESK_DATA.SHEMA sh on zp.id_shema = sh.id
            INNER JOIN APP_ADMIN.ESK_USERS u on c.id_uporabnik = u.user_id, binds b
         where 1=1 ';

        if v_query is not null then
          v_where := v_where || ' and (lower(sub.kmgmid) = b.v_query or lower(sub.maticna) = b.v_query or lower(sub.davcna) = b.v_query or lower(sub.naziv) like ''%''||b.v_query||''%'' or lower(sh.naziv) like ''%''||b.v_query||''%'' or lower(p.stevilka) = b.v_query or lower(c.stevilka) = b.v_query or lower(zp.naziv) like ''%''||b.v_query||''%'' or lower(clan.kmgmid) = b.v_query or lower(clan.naziv) like ''%''||b.v_query||''%'' or lower(clan.maticna) = b.v_query or lower(clan.davcna) = b.v_query)';
        end if;

        if v_query is not null and REGEXP_INSTR(v_query, '[[:digit:]]') > 0 then
          v_where := v_where || ' or sub.maticna = b.v_query or sub.davcna = b.v_query';
        end if;

        if v_organizacija is not null then
          v_where := v_where || ' and c.organizacija = upper(b.v_organizacija)';
        end if;

        open my_cursor for v_base_query || v_where
        using
            lower(nvl(v_query, '')),
            v_organizacija;
    END getPrilogaClanList;

    PROCEDURE getPrilogaProizvodList
    (
        v_query varchar2 default null, -- 24
        v_organizacija varchar2 default null, -- 25
        my_cursor OUT SYS_REFCURSOR
    )
    IS
        v_base_query clob;
        v_where clob;
        v_proizvodi_join clob;
    BEGIN
        v_base_query := '
        with binds as
        (
            select
                :bind1 as v_query,
                :bind2 as v_organizacija
            from dual
        ) -- !!! BE CAREFULL TO ADD NEW RESULT COLUMN. THEN YOU NEED TO ADD PARSING IN ANGULAR ALSO!! ADDING IT TO THE END EVERY TIME!!!!!
        select
                        cpp.id_priloga as id,
                        sub.kmgmid as kmgmid_imetnika,
                        case when sub.naziv is null then sub.ime || '' '' || sub.priimek else sub.naziv end as naziv_imetnika,
            sub.naslov || '', '' || sub.id_poste || '' '' || sub.posta as naslov_imetnika,
                        c.stevilka as stevilka_certifikata,
                        sh.naziv as shema,
                        zp.naziv as naziv_zascita,
                        pro.naziv as naziv_proizvod,
                        p.stevilka as stevilka,
                        TO_CHAR(p.DAT_IZDAJE, ''YYYY-MM-DD'') AS DAT_IZDAJE,
                        TO_CHAR(p.DAT_VELJ, ''YYYY-MM-DD'') AS DAT_VELJ,
                        p.status as status,
            c.id as id_certifikat,
            TO_CHAR(c.DAT_VNOSA, ''YYYY-MM-DD'') AS DAT_VNOSA
                from
                        ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD cpp
            inner join ESK_DATA.PRILOGA p on cpp.id_priloga = p.id
            inner join ESK_DATA.CERTIFIKAT c on cpp.id_certifikat = c.id
            inner join ESK_DATA.SUBJEKT sub on c.id_imetnik = sub.id
            inner join ESK_DATA.ZASCITENPROIZVOD zp on c.id_zasciten_proizvod = zp.id
            inner join ESK_DATA.SHEMA sh on zp.id_shema = sh.id
            left join ESK_DATA.CERTIFIKAT_PROIZVOD cp on c.id = cp.id_certifikat
            left join ESK_DATA.PROIZVOD pro on cp.id_proizvod = pro.id
            INNER JOIN APP_ADMIN.ESK_USERS u on c.id_uporabnik = u.user_id, binds b
         where 1=1 ';

        if v_query is not null then
          v_where := v_where || ' and (lower(sub.kmgmid) = b.v_query or lower(sub.naziv) like ''%''||b.v_query||''%'' or lower(sh.naziv) like ''%''||b.v_query||''%'' or lower(p.stevilka) = b.v_query or lower(c.stevilka) = b.v_query or lower(zp.naziv) like ''%''||b.v_query||''%'' )';
        end if;

        if v_query is not null and REGEXP_INSTR(v_query, '[[:digit:]]') > 0 then
          v_where := v_where || ' or sub.maticna = b.v_query or sub.davcna = b.v_query';
        end if;

        if v_organizacija is not null then
          v_where := v_where || ' and c.organizacija = upper(b.v_organizacija)';
        end if;

        open my_cursor for v_base_query || v_where
        using
            lower(nvl(v_query, '')),
            v_organizacija;
    END getPrilogaProizvodList;

    PROCEDURE updateCertificateAttachments(id_cert IN number, v_id_user number) IS
        v_dat_velj date null;
        v_max_id number null;
        v_parent_id number null;
    BEGIN
        select c.id_parent into v_parent_id from ESK_DATA.CERTIFIKAT c where c.id = id_cert;
        v_parent_id := nvl(v_parent_id, id_cert);

        select max(a.id) into v_max_id from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id);
        dbms_output.put_line('začenjam');
        if (NVL(v_max_id, 0) != 0) then
            dbms_output.put_line('priloga_clan');
            update ESK_DATA.PRILOGA
            set status = 'Neveljaven', spremenil = v_id_user
            where id in (
                select a.id from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.id != v_max_id
            );

            /* Get last invalid attachment, which need to be updated from current attachment */
            select max(a.id) into v_max_id from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.id != v_max_id;

            /* if no need to update previous attachments */
            if (NVL(v_max_id, 0) != 0) then
                /* Get current attachment dat_izdaje field, which need to be passed on previous attachment as dat_velj*/
                select max(a.dat_izdaje) into v_dat_velj from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.status = 'Veljaven';
                dbms_output.put_line('get dat izdaje');
                /* if there is no valid attachments */
                if (v_dat_velj is not null) then
                     dbms_output.put_line('update dat velj');
                    update ESK_DATA.PRILOGA set dat_velj = v_dat_velj, spremenil = v_id_user where id = v_max_id;
                end if;
            end if;
        end if;

        select max(a.id) into v_max_id from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id);
        if (NVL(v_max_id, 0) != 0) then
            dbms_output.put_line('priloga_proizvod');
            update ESK_DATA.PRILOGA
            set status = 'Neveljaven', spremenil = v_id_user
            where id in (
                select a.id from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.id != v_max_id
            );
            /* Get last invalid attachment, which need to be updated from current attachment */
            select max(a.id) into v_max_id from ESK_DATA.PRILOGA a inner join  ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.id != v_max_id;

            /* if no need to update previous attachments */
            if (NVL(v_max_id, 0) != 0) then
                /* Get current attachment dat_izdaje field, which need to be passed on previous attachment as dat_velj*/
                select max(a.dat_izdaje) into v_dat_velj from ESK_DATA.PRILOGA a inner join  ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.status = 'Veljaven';
                 dbms_output.put_line('get dat izdaje');
                /* if there is no valid attachments */
                if (v_dat_velj is not null) then
                    dbms_output.put_line('update dat velj');
                    update ESK_DATA.PRILOGA set dat_velj = v_dat_velj, spremenil = v_id_user where id = v_max_id;
                end if;
            end if;
        end if;
    END updateCertificateAttachments;

    PROCEDURE saveCertificateAttachment(v_id_cert NUMBER, v_id_priloga NUMBER, v_id OUT number) IS
    BEGIN
        select NVL(id, 0) into v_id from ESK_DATA.CERTIFIKAT_PRILOGA_CLAN where id_certifikat = v_id_cert and id_priloga = v_id_priloga;

        if (NVL(v_id, 0) = 0) then
            insert into ESK_DATA.CERTIFIKAT_PRILOGA_CLAN(id, id_certifikat, id_priloga) values(ESK_DATA.CERTIFIKAT_PRILOGA_CLAN_seq.nextval, v_id_cert, v_id_priloga)
            returning id into v_id;
        end if;
    END saveCertificateAttachment;

    PROCEDURE saveAttachmentMember(v_id_subj NUMBER, v_id_priloga NUMBER, v_id OUT number) IS
    BEGIN
        select NVL(id, 0) into v_id from ESK_DATA.PRILOGA_CLAN where id_subjekt = v_id_subj and id_priloga = v_id_priloga;

        if (NVL(v_id, 0) = 0) then
            insert into ESK_DATA.PRILOGA_CLAN(id, id_subjekt, id_priloga) values(ESK_DATA.PRILOGA_CLAN_seq.nextval, v_id_subj, v_id_priloga)
            returning id into v_id;
        end if;
    END saveAttachmentMember;

    PROCEDURE saveProduct(v_id_cert NUMBER, v_id_product NUMBER, v_id_user NUMBER, v_id OUT number) IS
    BEGIN
        insert into ESK_DATA.CERTIFIKAT_PROIZVOD(id, id_certifikat, id_proizvod, spremenil) values(ESK_DATA.CERTIFIKAT_PROIZVOD_seq.nextval, v_id_cert, v_id_product, v_id_user)
        returning id into v_id;
    END saveProduct;

    PROCEDURE saveActivity(v_id_cert NUMBER, v_id_activity NUMBER, v_id_user NUMBER, v_id OUT number) IS
    BEGIN
        insert into ESK_DATA.CERTIFIKAT_DEJAVNOST(id, id_certifikat, id_dejavnost, spremenil) values(ESK_DATA.CERTIFIKAT_DEJAVNOST_seq.nextval, v_id_cert, v_id_activity, v_id_user)
        returning id into v_id;    
    END saveActivity;

    PROCEDURE deleteAttachmentMembers( v_id_priloga NUMBER, v_id_user NUMBER) IS
    BEGIN
        delete from ESK_DATA.PRILOGA_CLAN where id_priloga = v_id_priloga;
                update ESK_DATA.PRILOGA_CLAN_HIST set spremenil = v_id_user where id_priloga = v_id_priloga and akcija = 'D';
    END deleteAttachmentMembers;

    PROCEDURE getPrevAttachment(id_cert in NUMBER, ptype in NUMBER, id_cert_priloga out NUMBER)
    as
        v_parent_id number null;
    BEGIN
        select c.id_parent into v_parent_id from ESK_DATA.CERTIFIKAT c where c.id = id_cert;
        v_parent_id := nvl(v_parent_id, id_cert);
        id_cert_priloga := 0;

        if (ptype = 0) then
            select nvl(max(b.id), 0) into id_cert_priloga from ESK_DATA.PRILOGA a inner join  ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.status != 'Veljaven';

            /*if (nvl(id_cert_priloga, 0) = 0) then
                select nvl(max(b.id), 0) into id_cert_priloga from ESK_DATA.PRILOGA a inner join  ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD b on a.id = b.id_priloga inner join ESK_DATA.CERTIFIKAT c on b.id_certifikat = c.id where b.id_certifikat = c.id_parent;
                if (nvl(id_cert_priloga, 0) = 0) then
                     id_cert_priloga := 0;
                end if;
            end if;*/
        end if;

        if (ptype = 1) then
            select nvl(max(b.id), 0) into id_cert_priloga from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN b on a.id = b.id_priloga where (b.id_certifikat = id_cert or b.id_certifikat = v_parent_id) and a.status != 'Veljaven';

            /*if (nvl(id_cert_priloga, 0) = 0) then
                select nvl(max(b.id), 0) into id_cert_priloga from ESK_DATA.PRILOGA a inner join ESK_DATA.CERTIFIKAT_PRILOGA_CLAN b on a.id = b.id_priloga inner join ESK_DATA.CERTIFIKAT c on b.id_certifikat = c.id where b.id_certifikat = c.id_parent;
                if (nvl(id_cert_priloga, 0) = 0) then
                     id_cert_priloga := 0;
                end if;
            end if;*/
        end if;

        id_cert_priloga := nvl(id_cert_priloga, 0);
    END getPrevAttachment;

    PROCEDURE invalidateCertificates IS
    BEGIN
        update ESK_DATA.CERTIFIKAT
        set status = 'Neveljaven', spremenil = 0
        where status = 'Veljaven' and dat_velj < CURRENT_DATE;
    END invalidateCertificates;


    PROCEDURE getReportData(v_report in varchar2, v_year in number, v_mytable out SYS_REFCURSOR) is
    begin
        OPEN v_mytable FOR
        select
            a.report, a.name, a.value, a.unit
        from (
            select 'RPT1' as report, s.naziv as name, count(a.id_shema) as value, null as unit
            from (select id_shema, id_subjekt, leto from ESK_DATA.evReport group by id_shema, id_subjekt, leto) a inner join ESK_DATA.SHEMA s on a.id_shema = s.id
            WHERE (v_report = 'RPT1' or v_report = 'RPTALL') and a.leto = v_year
            group by s.naziv
            UNION ALL
            SELECT 'RPT2' as report, zp.naziv as name, count(a.id_zasciten_proizvod) as value, null as unit
            from (select id_zasciten_proizvod, id_subjekt, leto from ESK_DATA.evReport group by id_zasciten_proizvod, id_subjekt, leto) a inner join ESK_DATA.ZASCITENPROIZVOD zp on a.id_zasciten_proizvod = zp.id
            WHERE (v_report = 'RPT2' or v_report = 'RPTALL') and a.leto = v_year
            group by zp.naziv
            UNION ALL
            select 'RPT3' as report, s.naziv as name, count(a.kmgmid) as value, null as unit
            from (
                select a.id_shema, b.kmgmid, a.leto
                from ESK_DATA.evReportWithMebers  a
                inner join ESK_DATA.SUBJEKT b on a.id_subjekt = b.id
                where b.kmgmid is not null
                group by a.id_shema, b.kmgmid, a.leto) a  
            inner join ESK_DATA.SHEMA s on a.id_shema = s.id
            WHERE (v_report = 'RPT1' or v_report = 'RPTALL') and a.leto = v_year
            group by s.naziv
            UNION ALL
            SELECT 'RPT4' as report, zp.naziv as name, count(a.kmgmid) as value, null as unit
            from (
                select a.id_zasciten_proizvod, b.kmgmid, a.leto
                from ESK_DATA.evReportWithMebers  a
                inner join ESK_DATA.SUBJEKT b on a.id_subjekt = b.id
                where b.kmgmid is not null
                group by a.id_zasciten_proizvod, b.kmgmid, a.leto) a
            inner join ESK_DATA.ZASCITENPROIZVOD zp on a.id_zasciten_proizvod = zp.id
            WHERE (v_report = 'RPT2' or v_report = 'RPTALL') and a.leto = v_year
            group by zp.naziv
            union all
            select 'RPT5' as report, a.shema || ' - ' || a.zascitenproizvod as name, sum(a.vrednost) as value, a.enota as unit
            from ESK_DATA.PROIZVODKOLICINE a
            WHERE (v_report = 'RPT5' or v_report = 'RPTALL') and a.leto = v_year and lower(a.enota) = 'ar'
            group by a.shema || ' - ' || a.zascitenproizvod, a.enota
            union all
            select 'RPT6' as report, a.shema || ' - ' || a.zascitenproizvod as name, sum(a.vrednost) as value, a.enota as unit
            from ESK_DATA.PROIZVODKOLICINE a
            WHERE (v_report = 'RPT6' or v_report = 'RPTALL') and a.leto = v_year and lower(a.enota) != 'ar'
            group by a.shema || ' - ' || a.zascitenproizvod, a.enota
        ) a
        order by a.report, a.name, a.value;
    end getReportData;

    
    PROCEDURE getKolicineList
    (
        v_query varchar2 default null,
        my_cursor OUT SYS_REFCURSOR
    )
    IS
        v_base_query clob;
        v_where clob;
        --v_proizvodi_join clob;
    BEGIN
        v_base_query := '
        with binds as
        (
            select
                :bind1 as v_query
            from dual
        ) -- !!! BE CAREFULL TO ADD NEW RESULT COLUMN. THEN YOU NEED TO ADD PARSING IN ANGULAR ALSO!! ADDING IT TO THE END EVERY TIME!!!!!
        select
            a.id, a.kmgmid as kmgmid, a.naziv_subj, a.naslov, a.id_poste, a.posta,
            a.vrednost, a.enota, a.leto, a.shema, a.zascitenproizvod, a.proizvod
        from
            ESK_DATA.PROIZVODKOLICINE a
            {{join}}, binds b
         where 1=1 ';

        if v_query is not null and REGEXP_INSTR(v_query, '[[:digit:]]') > 0 and LENGTH(v_query) != 9 then -- dolžina KMGMID = 9 znakov
            v_base_query := REPLACE(v_base_query, '{{join}}', 'left join ESK_DATA.evsubjekt sub on a.kmgmid = sub.kmgmid');
        else
             v_base_query := REPLACE(v_base_query, '{{join}}', '');
        end if;

        if v_query is not null then
          v_where := v_where || ' and (lower(a.kmgmid) = b.v_query or lower(a.naziv_subj) like ''%''||b.v_query||''%'' or lower(a.shema) like ''%''||b.v_query||''%'' or lower(a.zascitenproizvod) like ''%''||b.v_query||''%'' or lower(a.proizvod) like ''%''||b.v_query||''%'' )';
        end if;

        if v_query is not null and REGEXP_INSTR(v_query, '[[:digit:]]') > 0 and LENGTH(v_query) != 9 then
          v_where := v_where || ' and sub.maticna = b.v_query or sub.davcna = b.v_query';
        end if;

        open my_cursor for v_base_query || v_where
        using
            lower(nvl(v_query, ''));
    END getKolicineList;

BEGIN
    init := TRUE;
END;
/

