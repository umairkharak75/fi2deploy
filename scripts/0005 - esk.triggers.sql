create sequence ESK_DATA.CERTIFIKAT_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.CERTIFIKAT_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	TIP VARCHAR2(2000) NULL,
	STEVILKA VARCHAR2(2000) NULL,
	DAT_KONTROLE DATE NULL,
	DAT_IZDAJE DATE NULL,
	DAT_VELJ DATE NULL,
	STATUS VARCHAR2(2000) NULL,
	TEL_ST VARCHAR2(2000) NULL,
	EMAIL VARCHAR2(2000) NULL,
	OPOMBA VARCHAR2(2000) NULL,
	DAT_VNOSA DATE NULL,
	KONTROLOR VARCHAR2(2000) NULL,
	ID_ZASCITEN_PROIZVOD NUMBER,
	ID_IMETNIK NUMBER,
	ID_UPORABNIK NUMBER,
	ID_DEJAVNOST NUMBER,
	ORGANIZACIJA VARCHAR2(20),
	ID_PARENT NUMBER NULL,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.CERTIFIKAT_hist add constraint pk_CERTIFIKAT_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_CERTIFIKAT
before insert or delete or update on ESK_DATA.CERTIFIKAT
for each row
declare
    v_row CERTIFIKAT%rowtype;
    v_audit CERTIFIKAT_hist%rowtype;
begin
    select ESK_DATA.CERTIFIKAT_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.TIP := :new.TIP;
		v_audit.STEVILKA := :new.STEVILKA;
		v_audit.DAT_KONTROLE := :new.DAT_KONTROLE;
		v_audit.DAT_IZDAJE := :new.DAT_IZDAJE;
		v_audit.DAT_VELJ := :new.DAT_VELJ;
		v_audit.STATUS := :new.STATUS;
		v_audit.TEL_ST := :new.TEL_ST;
		v_audit.EMAIL := :new.EMAIL;
		v_audit.OPOMBA := :new.OPOMBA;
		v_audit.DAT_VNOSA := :new.DAT_VNOSA;
		v_audit.KONTROLOR := :new.KONTROLOR;
		v_audit.ID_ZASCITEN_PROIZVOD := :new.ID_ZASCITEN_PROIZVOD;
		v_audit.ID_IMETNIK := :new.ID_IMETNIK;
		v_audit.ID_UPORABNIK := :new.ID_UPORABNIK;
		v_audit.ID_DEJAVNOST := :new.ID_DEJAVNOST;
		v_audit.ORGANIZACIJA := :new.ORGANIZACIJA;
		v_audit.ID_PARENT := :new.ID_PARENT;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.TIP := :old.TIP;
		v_audit.STEVILKA := :old.STEVILKA;
		v_audit.DAT_KONTROLE := :old.DAT_KONTROLE;
		v_audit.DAT_IZDAJE := :old.DAT_IZDAJE;
		v_audit.DAT_VELJ := :old.DAT_VELJ;
		v_audit.STATUS := :old.STATUS;
		v_audit.TEL_ST := :old.TEL_ST;
		v_audit.EMAIL := :old.EMAIL;
		v_audit.OPOMBA := :old.OPOMBA;
		v_audit.DAT_VNOSA := :old.DAT_VNOSA;
		v_audit.KONTROLOR := :old.KONTROLOR;
		v_audit.ID_ZASCITEN_PROIZVOD := :old.ID_ZASCITEN_PROIZVOD;
		v_audit.ID_IMETNIK := :old.ID_IMETNIK;
		v_audit.ID_UPORABNIK := :old.ID_UPORABNIK;
		v_audit.ID_DEJAVNOST := :old.ID_DEJAVNOST;
		v_audit.ORGANIZACIJA := :old.ORGANIZACIJA;
		v_audit.ID_PARENT := :old.ID_PARENT;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.CERTIFIKAT_hist values v_audit;
end trig_CERTIFIKAT;
/
create sequence ESK_DATA.CERTIFIKAT_DEJAVNOST_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.CERTIFIKAT_DEJAVNOST_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_CERTIFIKAT NUMBER,
	ID_DEJAVNOST NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.CERTIFIKAT_DEJAVNOST_hist add constraint pk_CERTIFIKAT_DEJAVNOST_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_CERTIFIKAT_DEJAVNOST
before insert or delete or update on ESK_DATA.CERTIFIKAT_DEJAVNOST
for each row
declare
    v_row CERTIFIKAT_DEJAVNOST%rowtype;
    v_audit CERTIFIKAT_DEJAVNOST_hist%rowtype;
begin
    select ESK_DATA.CERTIFIKAT_DEJAVNOST_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_CERTIFIKAT := :new.ID_CERTIFIKAT;
		v_audit.ID_DEJAVNOST := :new.ID_DEJAVNOST;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_CERTIFIKAT := :old.ID_CERTIFIKAT;
		v_audit.ID_DEJAVNOST := :old.ID_DEJAVNOST;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.CERTIFIKAT_DEJAVNOST_hist values v_audit;
end trig_CERTIFIKAT_DEJAVNOST;
/
create sequence ESK_DATA.CERTIFIKAT_EXT_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.CERTIFIKAT_EXT_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_CERTIFIKAT NUMBER,
	ID_IMIS VARCHAR2(2000),
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.CERTIFIKAT_EXT_hist add constraint pk_CERTIFIKAT_EXT_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_CERTIFIKAT_EXT
before insert or delete or update on ESK_DATA.CERTIFIKAT_EXT
for each row
declare
    v_row CERTIFIKAT_EXT%rowtype;
    v_audit CERTIFIKAT_EXT_hist%rowtype;
begin
    select ESK_DATA.CERTIFIKAT_EXT_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_CERTIFIKAT := :new.ID_CERTIFIKAT;
		v_audit.ID_IMIS := :new.ID_IMIS;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_CERTIFIKAT := :old.ID_CERTIFIKAT;
		v_audit.ID_IMIS := :old.ID_IMIS;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.CERTIFIKAT_EXT_hist values v_audit;
end trig_CERTIFIKAT_EXT;
/
create sequence ESK_DATA.CERT_PRIL_CLAN_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.CERTIFIKAT_PRILOGA_CLAN_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_CERTIFIKAT NUMBER,
	ID_PRILOGA NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.CERTIFIKAT_PRILOGA_CLAN_hist add constraint pk_CERT_PRIL_CLAN_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_CERTIFIKAT_PRILOGA_CLAN
before insert or delete or update on ESK_DATA.CERTIFIKAT_PRILOGA_CLAN
for each row
declare
    v_row CERTIFIKAT_PRILOGA_CLAN%rowtype;
    v_audit CERTIFIKAT_PRILOGA_CLAN_hist%rowtype;
begin
    select ESK_DATA.CERT_PRIL_CLAN_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_CERTIFIKAT := :new.ID_CERTIFIKAT;
		v_audit.ID_PRILOGA := :new.ID_PRILOGA;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_CERTIFIKAT := :old.ID_CERTIFIKAT;
		v_audit.ID_PRILOGA := :old.ID_PRILOGA;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.CERTIFIKAT_PRILOGA_CLAN_hist values v_audit;
end trig_CERTIFIKAT_PRILOGA_CLAN;
/
create sequence ESK_DATA.CERT_PRIL_PRO_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.CERT_PRIL_PRO_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_CERTIFIKAT NUMBER,
	ID_PRILOGA NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.CERT_PRIL_PRO_hist add constraint pk_CERT_PRIL_PRO_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_CERT_PRIL_PRO
before insert or delete or update on ESK_DATA.CERTIFIKAT_PRILOGA_PROIZVOD
for each row
declare
    v_row CERTIFIKAT_PRILOGA_PROIZVOD%rowtype;
    v_audit CERT_PRIL_PRO_hist%rowtype;
begin
    select ESK_DATA.CERT_PRIL_PRO_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_CERTIFIKAT := :new.ID_CERTIFIKAT;
		v_audit.ID_PRILOGA := :new.ID_PRILOGA;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_CERTIFIKAT := :old.ID_CERTIFIKAT;
		v_audit.ID_PRILOGA := :old.ID_PRILOGA;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.CERT_PRIL_PRO_hist values v_audit;
end trig_CERT_PRIL_PRO;
/
create sequence ESK_DATA.CERTIFIKAT_PROIZVOD_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.CERTIFIKAT_PROIZVOD_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_CERTIFIKAT NUMBER,
	ID_PROIZVOD NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.CERTIFIKAT_PROIZVOD_hist add constraint pk_CERTIFIKAT_PROIZVOD_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_CERTIFIKAT_PROIZVOD
before insert or delete or update on ESK_DATA.CERTIFIKAT_PROIZVOD
for each row
declare
    v_row CERTIFIKAT_PROIZVOD%rowtype;
    v_audit CERTIFIKAT_PROIZVOD_hist%rowtype;
begin
    select ESK_DATA.CERTIFIKAT_PROIZVOD_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_CERTIFIKAT := :new.ID_CERTIFIKAT;
		v_audit.ID_PROIZVOD := :new.ID_PROIZVOD;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_CERTIFIKAT := :old.ID_CERTIFIKAT;
		v_audit.ID_PROIZVOD := :old.ID_PROIZVOD;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.CERTIFIKAT_PROIZVOD_hist values v_audit;
end trig_CERTIFIKAT_PROIZVOD;
/
create sequence ESK_DATA.DEJAVNOST_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.DEJAVNOST_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	NAZIV VARCHAR2(2000) NULL,
	NEAKTIVEN NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.DEJAVNOST_hist add constraint pk_DEJAVNOST_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_DEJAVNOST
before insert or delete or update on ESK_DATA.DEJAVNOST
for each row
declare
    v_row DEJAVNOST%rowtype;
    v_audit DEJAVNOST_hist%rowtype;
begin
    select ESK_DATA.DEJAVNOST_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.NAZIV := :new.NAZIV;
		v_audit.NEAKTIVEN := :new.NEAKTIVEN;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.NAZIV := :old.NAZIV;
		v_audit.NEAKTIVEN := :old.NEAKTIVEN;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.DEJAVNOST_hist values v_audit;
end trig_DEJAVNOST;
/

create sequence ESK_DATA.PRILOGA_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.PRILOGA_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	STEVILKA VARCHAR2(2000),
	DAT_IZDAJE DATE NULL,
	DAT_VELJ DATE NULL,
	STATUS VARCHAR2(2000),
	VSEBINA CLOB NULL,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.PRILOGA_hist add constraint pk_PRILOGA_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_PRILOGA
before insert or delete or update on ESK_DATA.PRILOGA
for each row
declare
    v_row PRILOGA%rowtype;
    v_audit PRILOGA_hist%rowtype;
begin
    select ESK_DATA.PRILOGA_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.STEVILKA := :new.STEVILKA;
		v_audit.DAT_IZDAJE := :new.DAT_IZDAJE;
		v_audit.DAT_VELJ := :new.DAT_VELJ;
		v_audit.STATUS := :new.STATUS;
		v_audit.VSEBINA := :new.VSEBINA;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.STEVILKA := :old.STEVILKA;
		v_audit.DAT_IZDAJE := :old.DAT_IZDAJE;
		v_audit.DAT_VELJ := :old.DAT_VELJ;
		v_audit.STATUS := :old.STATUS;
		v_audit.VSEBINA := :old.VSEBINA;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.PRILOGA_hist values v_audit;
end trig_PRILOGA;
/
create sequence ESK_DATA.PRILOGA_CLAN_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.PRILOGA_CLAN_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_PRILOGA NUMBER,
	ID_SUBJEKT NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.PRILOGA_CLAN_hist add constraint pk_PRILOGA_CLAN_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_PRILOGA_CLAN
before insert or delete or update on ESK_DATA.PRILOGA_CLAN
for each row
declare
    v_row PRILOGA_CLAN%rowtype;
    v_audit PRILOGA_CLAN_hist%rowtype;
begin
    select ESK_DATA.PRILOGA_CLAN_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_PRILOGA := :new.ID_PRILOGA;
		v_audit.ID_SUBJEKT := :new.ID_SUBJEKT;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_PRILOGA := :old.ID_PRILOGA;
		v_audit.ID_SUBJEKT := :old.ID_SUBJEKT;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.PRILOGA_CLAN_hist values v_audit;
end trig_PRILOGA_CLAN;
/
create sequence ESK_DATA.PROIZVOD_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.PROIZVOD_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	NAZIV VARCHAR2(2000) NULL,
	ID_ZAKONSKA_PODLAGA NUMBER NULL,
	ID_ZASCITEN_PROIZVOD NUMBER,
	NEAKTIVEN NUMBER,
	ENOTA VARCHAR2(25),
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.PROIZVOD_hist add constraint pk_PROIZVOD_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_PROIZVOD
before insert or delete or update on ESK_DATA.PROIZVOD
for each row
declare
    v_row PROIZVOD%rowtype;
    v_audit PROIZVOD_hist%rowtype;
begin
    select ESK_DATA.PROIZVOD_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.NAZIV := :new.NAZIV;
		v_audit.ID_ZAKONSKA_PODLAGA := :new.ID_ZAKONSKA_PODLAGA;
		v_audit.ID_ZASCITEN_PROIZVOD := :new.ID_ZASCITEN_PROIZVOD;
		v_audit.NEAKTIVEN := :new.NEAKTIVEN;
		v_audit.ENOTA := :new.ENOTA;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.NAZIV := :old.NAZIV;
		v_audit.ID_ZAKONSKA_PODLAGA := :old.ID_ZAKONSKA_PODLAGA;
		v_audit.ID_ZASCITEN_PROIZVOD := :old.ID_ZASCITEN_PROIZVOD;
		v_audit.NEAKTIVEN := :old.NEAKTIVEN;
		v_audit.ENOTA := :old.ENOTA;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.PROIZVOD_hist values v_audit;
end trig_PROIZVOD;
/
create sequence ESK_DATA.PROIZVODKOLICINE_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.PROIZVODKOLICINE_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	VREDNOST NUMBER NULL,
	ENOTA VARCHAR2(2000) NULL,
	LETO NUMBER,
	KMGMID VARCHAR2(25) NULL,
	NAZIV_SUBJ VARCHAR2(250) NULL,
	NASLOV VARCHAR2(250) NULL,
	ID_POSTE VARCHAR2(25) NULL,
	POSTA VARCHAR2(250) NULL,
	DAVCNA VARCHAR2(25) NULL,
	MATICNA VARCHAR2(25) NULL,
	TEL_ST VARCHAR2(25) NULL,
	EMAIL VARCHAR2(250) NULL,
	SHEMA VARCHAR2(250) NULL,
	ZASCITENPROIZVOD VARCHAR2(250) NULL,
	PROIZVOD VARCHAR2(250) NULL,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.PROIZVODKOLICINE_hist add constraint pk_PROIZVODKOLICINE_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_PROIZVODKOLICINE
before insert or delete or update on ESK_DATA.PROIZVODKOLICINE
for each row
declare
    v_row PROIZVODKOLICINE%rowtype;
    v_audit PROIZVODKOLICINE_hist%rowtype;
begin
    select ESK_DATA.PROIZVODKOLICINE_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.VREDNOST := :new.VREDNOST;
		v_audit.ENOTA := :new.ENOTA;
		v_audit.LETO := :new.LETO;
		v_audit.KMGMID := :new.KMGMID;
		v_audit.NAZIV_SUBJ := :new.NAZIV_SUBJ;
		v_audit.NASLOV := :new.NASLOV;
		v_audit.ID_POSTE := :new.ID_POSTE;
		v_audit.POSTA := :new.POSTA;
		v_audit.DAVCNA := :new.DAVCNA;
		v_audit.MATICNA := :new.MATICNA;
		v_audit.TEL_ST := :new.TEL_ST;
		v_audit.EMAIL := :new.EMAIL;
		v_audit.SHEMA := :new.SHEMA;
		v_audit.ZASCITENPROIZVOD := :new.ZASCITENPROIZVOD;
		v_audit.PROIZVOD := :new.PROIZVOD;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.VREDNOST := :old.VREDNOST;
		v_audit.ENOTA := :old.ENOTA;
		v_audit.LETO := :old.LETO;
		v_audit.KMGMID := :old.KMGMID;
		v_audit.NAZIV_SUBJ := :old.NAZIV_SUBJ;
		v_audit.NASLOV := :old.NASLOV;
		v_audit.ID_POSTE := :old.ID_POSTE;
		v_audit.POSTA := :old.POSTA;
		v_audit.DAVCNA := :old.DAVCNA;
		v_audit.MATICNA := :old.MATICNA;
		v_audit.TEL_ST := :old.TEL_ST;
		v_audit.EMAIL := :old.EMAIL;
		v_audit.SHEMA := :old.SHEMA;
		v_audit.ZASCITENPROIZVOD := :old.ZASCITENPROIZVOD;
		v_audit.PROIZVOD := :old.PROIZVOD;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.PROIZVODKOLICINE_hist values v_audit;
end trig_PROIZVODKOLICINE;
/
create sequence ESK_DATA.SHEMA_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.SHEMA_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	NAZIV VARCHAR2(2000) NULL,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.SHEMA_hist add constraint pk_SHEMA_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_SHEMA
before insert or delete or update on ESK_DATA.SHEMA
for each row
declare
    v_row SHEMA%rowtype;
    v_audit SHEMA_hist%rowtype;
begin
    select ESK_DATA.SHEMA_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.NAZIV := :new.NAZIV;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.NAZIV := :old.NAZIV;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.SHEMA_hist values v_audit;
end trig_SHEMA;
/
create sequence ESK_DATA.SUBJEKT_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.SUBJEKT_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_SUBJ NUMBER,
	KMGMID VARCHAR2(2000) NULL,
	IME VARCHAR2(2000) NULL,
	PRIIMEK VARCHAR2(2000) NULL,
	NAZIV VARCHAR2(2000) NULL,
	NASLOV VARCHAR2(2000) NULL,
	MATICNA VARCHAR2(2000) NULL,
	DAVCNA VARCHAR2(2000) NULL,
	ID_POSTE VARCHAR2(2000) NULL,
	POSTA VARCHAR2(2000) NULL,
	TEL_ST VARCHAR2(2000) NULL,
	EMAIL VARCHAR2(2000) NULL,
	OB_ID VARCHAR2(2000) NULL,
	OBCINA VARCHAR2(2000) NULL,
	DAT_ZS DATE,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.SUBJEKT_hist add constraint pk_SUBJEKT_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_SUBJEKT
before insert or delete or update on ESK_DATA.SUBJEKT
for each row
declare
    v_row SUBJEKT%rowtype;
    v_audit SUBJEKT_hist%rowtype;
begin
    select ESK_DATA.SUBJEKT_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_SUBJ := :new.ID_SUBJ;
		v_audit.KMGMID := :new.KMGMID;
		v_audit.IME := :new.IME;
		v_audit.PRIIMEK := :new.PRIIMEK;
		v_audit.NAZIV := :new.NAZIV;
		v_audit.NASLOV := :new.NASLOV;
		v_audit.MATICNA := :new.MATICNA;
		v_audit.DAVCNA := :new.DAVCNA;
		v_audit.ID_POSTE := :new.ID_POSTE;
		v_audit.POSTA := :new.POSTA;
		v_audit.TEL_ST := :new.TEL_ST;
		v_audit.EMAIL := :new.EMAIL;
		v_audit.OB_ID := :new.OB_ID;
		v_audit.OBCINA := :new.OBCINA;
		v_audit.DAT_ZS := :new.DAT_ZS;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_SUBJ := :old.ID_SUBJ;
		v_audit.KMGMID := :old.KMGMID;
		v_audit.IME := :old.IME;
		v_audit.PRIIMEK := :old.PRIIMEK;
		v_audit.NAZIV := :old.NAZIV;
		v_audit.NASLOV := :old.NASLOV;
		v_audit.MATICNA := :old.MATICNA;
		v_audit.DAVCNA := :old.DAVCNA;
		v_audit.ID_POSTE := :old.ID_POSTE;
		v_audit.POSTA := :old.POSTA;
		v_audit.TEL_ST := :old.TEL_ST;
		v_audit.EMAIL := :old.EMAIL;
		v_audit.OB_ID := :old.OB_ID;
		v_audit.OBCINA := :old.OBCINA;
		v_audit.DAT_ZS := :old.DAT_ZS;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.SUBJEKT_hist values v_audit;
end trig_SUBJEKT;
/
create sequence ESK_DATA.ZAKONSKAPODLAGA_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.ZAKONSKAPODLAGA_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	STEVILKA VARCHAR2(2000) NULL,
	VSEBINA CLOB NULL,
	NEAKTIVEN NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.ZAKONSKAPODLAGA_hist add constraint pk_ZAKONSKAPODLAGA_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_ZAKONSKAPODLAGA
before insert or delete or update on ESK_DATA.ZAKONSKAPODLAGA
for each row
declare
    v_row ZAKONSKAPODLAGA%rowtype;
    v_audit ZAKONSKAPODLAGA_hist%rowtype;
begin
    select ESK_DATA.ZAKONSKAPODLAGA_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.STEVILKA := :new.STEVILKA;
		v_audit.VSEBINA := :new.VSEBINA;
		v_audit.NEAKTIVEN := :new.NEAKTIVEN;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.STEVILKA := :old.STEVILKA;
		v_audit.VSEBINA := :old.VSEBINA;
		v_audit.NEAKTIVEN := :old.NEAKTIVEN;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.ZAKONSKAPODLAGA_hist values v_audit;
end trig_ZAKONSKAPODLAGA;
/
create sequence ESK_DATA.ZASCITENPROIZVOD_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.ZASCITENPROIZVOD_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	NAZIV VARCHAR2(2000) NULL,
	ID_SHEMA NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.ZASCITENPROIZVOD_hist add constraint pk_ZASCITENPROIZVOD_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_ZASCITENPROIZVOD
before insert or delete or update on ESK_DATA.ZASCITENPROIZVOD
for each row
declare
    v_row ZASCITENPROIZVOD%rowtype;
    v_audit ZASCITENPROIZVOD_hist%rowtype;
begin
    select ESK_DATA.ZASCITENPROIZVOD_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.NAZIV := :new.NAZIV;
		v_audit.ID_SHEMA := :new.ID_SHEMA;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.NAZIV := :old.NAZIV;
		v_audit.ID_SHEMA := :old.ID_SHEMA;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.ZASCITENPROIZVOD_hist values v_audit;
end trig_ZASCITENPROIZVOD;
/
create sequence ESK_DATA.ZASCITNIZNAK_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.ZASCITNIZNAK_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ZZ_SHEMA VARCHAR2(2000) NULL,
	NAZIV_PROIZVODA VARCHAR2(2000) NULL,
	DAT_ODL DATE NULL,
	ST_ODL VARCHAR2(2000) NULL,
	CERT_ORGAN VARCHAR2(2000) NULL,
	ID_IMETNIK NUMBER,
	ID_ZASCITEN_PROIZVOD NUMBER NULL,
	NEAKTIVEN NUMBER,
	STEVILKA VARCHAR2(25),
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.ZASCITNIZNAK_hist add constraint pk_ZASCITNIZNAK_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_ZASCITNIZNAK
before insert or delete or update on ESK_DATA.ZASCITNIZNAK
for each row
declare
    v_row ZASCITNIZNAK%rowtype;
    v_audit ZASCITNIZNAK_hist%rowtype;
begin
    select ESK_DATA.ZASCITNIZNAK_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ZZ_SHEMA := :new.ZZ_SHEMA;
		v_audit.NAZIV_PROIZVODA := :new.NAZIV_PROIZVODA;
		v_audit.DAT_ODL := :new.DAT_ODL;
		v_audit.ST_ODL := :new.ST_ODL;
		v_audit.CERT_ORGAN := :new.CERT_ORGAN;
		v_audit.ID_IMETNIK := :new.ID_IMETNIK;
		v_audit.ID_ZASCITEN_PROIZVOD := :new.ID_ZASCITEN_PROIZVOD;
		v_audit.NEAKTIVEN := :new.NEAKTIVEN;
		v_audit.STEVILKA := :new.STEVILKA;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ZZ_SHEMA := :old.ZZ_SHEMA;
		v_audit.NAZIV_PROIZVODA := :old.NAZIV_PROIZVODA;
		v_audit.DAT_ODL := :old.DAT_ODL;
		v_audit.ST_ODL := :old.ST_ODL;
		v_audit.CERT_ORGAN := :old.CERT_ORGAN;
		v_audit.ID_IMETNIK := :old.ID_IMETNIK;
		v_audit.ID_ZASCITEN_PROIZVOD := :old.ID_ZASCITEN_PROIZVOD;
		v_audit.NEAKTIVEN := :old.NEAKTIVEN;
		v_audit.STEVILKA := :old.STEVILKA;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.ZASCITNIZNAK_hist values v_audit;
end trig_ZASCITNIZNAK;
/
create sequence ESK_DATA.ZZ_PROIZVOD_hist_seq start with 1000 increment by 1 nocache nocycle;
/

create table ESK_DATA.ZASCITNIZNAK_PROIZVOD_hist (
    verzija NUMBER(10),
    akcija VARCHAR2(1),
	ID NUMBER(10),
	ID_ZASCITNIZNAK NUMBER,
	ID_PROIZVOD NUMBER,
	SPREMENIL NUMBER NULL,
	DAT_SPREMEMBE DATE NULL
);
/

alter table ESK_DATA.ZASCITNIZNAK_PROIZVOD_hist add constraint pk_ZASCITNIZNAK_PROIZVOD_hist primary key(id, verzija);
/

create or replace trigger ESK_DATA.trig_ZASCITNIZNAK_PROIZVOD
before insert or delete or update on ESK_DATA.ZASCITNIZNAK_PROIZVOD
for each row
declare
    v_row ZASCITNIZNAK_PROIZVOD%rowtype;
    v_audit ZASCITNIZNAK_PROIZVOD_hist%rowtype;
begin
    select ESK_DATA.ZZ_PROIZVOD_hist_seq.nextval into v_audit.verzija from dual;

    if not deleting then
        :new.DAT_SPREMEMBE := sysdate;
    end if;    
    
    if inserting or updating then
		v_audit.ID := :new.ID;
		v_audit.ID_ZASCITNIZNAK := :new.ID_ZASCITNIZNAK;
		v_audit.ID_PROIZVOD := :new.ID_PROIZVOD;
		v_audit.SPREMENIL := :new.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :new.DAT_SPREMEMBE;
        v_audit.akcija:= case when inserting then 'I' else 'U' end;
    elsif deleting then
		v_audit.ID := :old.ID;
		v_audit.ID_ZASCITNIZNAK := :old.ID_ZASCITNIZNAK;
		v_audit.ID_PROIZVOD := :old.ID_PROIZVOD;
		v_audit.SPREMENIL := :old.SPREMENIL;
		v_audit.DAT_SPREMEMBE := :old.DAT_SPREMEMBE;
        v_audit.akcija:= 'D';
    end if;

    insert into ESK_DATA.ZASCITNIZNAK_PROIZVOD_hist values v_audit;
end trig_ZASCITNIZNAK_PROIZVOD;
/
