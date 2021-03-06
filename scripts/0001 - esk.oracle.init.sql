CREATE TABLE "ZAKONSKAPODLAGA" 
(	
	ID NUMBER(10) NOT NULL,
	STEVILKA VARCHAR2(2000),
	VSEBINA CLOB
);

ALTER TABLE ZAKONSKAPODLAGA ADD (CONSTRAINT ZAKONSKAPODLAGA_pk PRIMARY KEY (ID));

CREATE SEQUENCE ZAKONSKAPODLAGA_seq START WITH 1;
-------------------------------------------------------------------------------------
CREATE TABLE "SHEMA" 
(	
	ID NUMBER(10) NOT NULL,
	NAZIV VARCHAR2(2000)
);

ALTER TABLE SHEMA ADD (CONSTRAINT SHEMA_pk PRIMARY KEY (ID));

CREATE SEQUENCE SHEMA_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "ZASCITENPROIZVOD" 
(	
	ID NUMBER(10) NOT NULL,
	NAZIV VARCHAR2(2000),
    ID_SHEMA NUMBER NOT NULL
);

ALTER TABLE ZASCITENPROIZVOD ADD (CONSTRAINT ZASCITENPROIZVOD_pk PRIMARY KEY (ID));

CREATE SEQUENCE ZASCITENPROIZVOD_seq START WITH 1;

ALTER TABLE "ZASCITENPROIZVOD"
ADD CONSTRAINT "FK_ZP_IDSHEMA_SHEMA_ID"
   FOREIGN KEY (ID_SHEMA)
   REFERENCES "SHEMA" (ID);
-------------------------------------------------------------------------------------

CREATE TABLE "PROIZVOD" 
(	
	ID NUMBER(10) NOT NULL,
	NAZIV VARCHAR2(2000),
	ID_ZAKONSKA_PODLAGA NUMBER NULL,
	ID_ZASCITEN_PROIZVOD NUMBER NOT NULL
);

ALTER TABLE "PROIZVOD" 
ADD CONSTRAINT "FK_PROIZVOD_ZP_ID_1"
   FOREIGN KEY (ID_ZAKONSKA_PODLAGA)
   REFERENCES "ZAKONSKAPODLAGA" (ID);

ALTER TABLE "PROIZVOD"
ADD CONSTRAINT "FK_PROIZVOD_ZP_ID_2"
   FOREIGN KEY (ID_ZASCITEN_PROIZVOD)
   REFERENCES "ZASCITENPROIZVOD" (ID);

ALTER TABLE PROIZVOD ADD (CONSTRAINT PROIZVOD_pk PRIMARY KEY (ID));

CREATE SEQUENCE PROIZVOD_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "SUBJEKT"
(
	ID NUMBER(10) NOT NULL,
    ID_SUBJ NUMBER NOT NULL,
	KMGMID VARCHAR2(2000) NULL,
	IME VARCHAR2(2000) NULL,
	PRIIMEK VARCHAR2(2000) NULL,
	NAZIV VARCHAR2(2000) NULL,
	NASLOV VARCHAR2(2000),
	MATICNA VARCHAR2(2000) NULL,
	DAVCNA VARCHAR2(2000) NULL,
	ID_POSTE VARCHAR2(2000),
	POSTA VARCHAR2(2000),
	TEL_ST VARCHAR2(2000) NULL,
	EMAIL VARCHAR2(2000) NULL,
    OB_ID VARCHAR2(2000) NULL,
    OBCINA VARCHAR2(2000) NULL,
    DAT_ZS DATE NOT NULL
);

ALTER TABLE SUBJEKT ADD (CONSTRAINT SUBJEKT_pk PRIMARY KEY (ID));

CREATE SEQUENCE SUBJEKT_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "PRAVICA"
(
	ID NUMBER(10) NOT NULL,
	NAZIV VARCHAR2(2000),
	TIP VARCHAR2(2000)
);
ALTER TABLE PRAVICA ADD (CONSTRAINT PRAVICA_pk PRIMARY KEY (ID));

CREATE SEQUENCE PRAVICA_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "DEJAVNOST"
(
	ID NUMBER(10) NOT NULL,
	NAZIV VARCHAR2(2000)
);

ALTER TABLE DEJAVNOST ADD (CONSTRAINT DEJAVNOST_pk PRIMARY KEY (ID));

CREATE SEQUENCE DEJAVNOST_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "ZASCITNIZNAK"
(
	ID NUMBER(10) NOT NULL,
	ZZ_SHEMA VARCHAR2(2000) NULL,
	NAZIV_PROIZVODA VARCHAR2(2000) NULL,
	DAT_ODL DATE NULL,
	ST_ODL VARCHAR2(2000) NULL,
	CERT_ORGAN VARCHAR2(2000) NULL,
	ID_IMETNIK NUMBER NOT NULL,
	ID_ZASCITEN_PROIZVOD NUMBER NULL
);

ALTER TABLE "ZASCITNIZNAK"
ADD CONSTRAINT "FK_ZASCITNIZNAK_1"
   FOREIGN KEY (ID_IMETNIK)
   REFERENCES "SUBJEKT" (ID);

ALTER TABLE "ZASCITNIZNAK"
ADD CONSTRAINT "FK_ZASCITNIZNAK_2"
   FOREIGN KEY (ID_ZASCITEN_PROIZVOD)
   REFERENCES "ZASCITENPROIZVOD" (ID);

ALTER TABLE ZASCITNIZNAK ADD (CONSTRAINT ZASCITNIZNAK_pk PRIMARY KEY (ID));

CREATE SEQUENCE ZASCITNIZNAK_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "ZASCITNIZNAK_PROIZVOD"
(
	ID NUMBER(10) NOT NULL,
	ID_ZASCITNIZNAK NUMBER NOT NULL,
	ID_PROIZVOD NUMBER NOT NULL
);
ALTER TABLE "ZASCITNIZNAK_PROIZVOD"
ADD CONSTRAINT "FK_ZZASCITNIZNAK_PROIZVOD_1"
   FOREIGN KEY (ID_ZASCITNIZNAK)
   REFERENCES "ZASCITNIZNAK" (ID);

ALTER TABLE "ZASCITNIZNAK_PROIZVOD"
ADD CONSTRAINT "FK_ZZASCITNIZNAK_PROIZVOD_2"
   FOREIGN KEY (ID_PROIZVOD)
   REFERENCES "PROIZVOD" (ID);

ALTER TABLE ZASCITNIZNAK_PROIZVOD ADD (CONSTRAINT ZASCITNIZNAK_PROIZVOD_pk PRIMARY KEY (ID));

CREATE SEQUENCE ZASCITNIZNAK_PROIZVOD_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "CERTIFIKAT"
(
	ID NUMBER(10) NOT NULL,
	TIP VARCHAR2(2000),
	STEVILKA VARCHAR2(2000),
	DAT_KONTROLE DATE NULL,
	DAT_IZDAJE DATE NULL,
	DAT_VELJ DATE NULL,
	STATUS VARCHAR2(2000),
	TEL_ST VARCHAR2(2000) NULL,
	EMAIL VARCHAR2(2000) NULL,
	OPOMBA VARCHAR2(2000) NULL,
	DAT_VNOSA DATE,
	KONTROLOR VARCHAR2(2000),
	ID_ZASCITEN_PROIZVOD NUMBER NOT NULL,
	ID_IMETNIK NUMBER NOT NULL,
	ID_UPORABNIK NUMBER NOT NULL,
	ID_DEJAVNOST NUMBER NOT NULL,
    ORGANIZACIJA VARCHAR2(20) NOT NULL
);

ALTER TABLE "CERTIFIKAT"
ADD CONSTRAINT "FK_CERTIFIKAT_1"
   FOREIGN KEY (ID_ZASCITEN_PROIZVOD)
   REFERENCES "ZASCITENPROIZVOD" (ID);

ALTER TABLE "CERTIFIKAT"
ADD CONSTRAINT "FK_CERTIFIKAT_2"
   FOREIGN KEY (ID_IMETNIK)
   REFERENCES "SUBJEKT" (ID);

ALTER TABLE "CERTIFIKAT"
ADD CONSTRAINT "FK_CERTIFIKAT_3"
   FOREIGN KEY (ID_DEJAVNOST)
   REFERENCES "DEJAVNOST" (ID);

ALTER TABLE CERTIFIKAT ADD (CONSTRAINT CERTIFIKAT_pk PRIMARY KEY (ID));

CREATE SEQUENCE CERTIFIKAT_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "PROIZVODKOLICINE"
(
	ID NUMBER(10) NOT NULL,
	VREDNOST NUMBER,
	ENOTA VARCHAR2(2000) NULL,
	LETO NUMBER NOT NULL,
	ID_PROIZVOD NUMBER NOT NULL
);
ALTER TABLE "PROIZVODKOLICINE"
ADD CONSTRAINT "FK_PROIZVODKOLICINE_1"
   FOREIGN KEY (ID_PROIZVOD)
   REFERENCES "PROIZVOD" (ID);

ALTER TABLE PROIZVODKOLICINE ADD (CONSTRAINT PROIZVODKOLICINE_pk PRIMARY KEY (ID));

CREATE SEQUENCE PROIZVODKOLICINE_seq START WITH 1;
-------------------------------------------------------------------------------------
CREATE TABLE "PRILOGA"
(
	ID NUMBER(10) NOT NULL,
	STEVILKA VARCHAR2(2000) NOT NULL,
	DAT_IZDAJE DATE NOT NULL,
	DAT_VELJ DATE NOT NULL,
	STATUS VARCHAR2(2000) NOT NULL,
	VSEBINA CLOB NULL
);

ALTER TABLE PRILOGA ADD (CONSTRAINT PRILOGA_pk PRIMARY KEY (ID));

CREATE SEQUENCE PRILOGA_seq START WITH 1;
-------------------------------------------------------------------------------------

CREATE TABLE "CERTIFIKAT_PRILOGA_CLAN"
(
	ID NUMBER(10) NOT NULL,
	ID_CERTIFIKAT NUMBER NOT NULL,
	ID_PRILOGA NUMBER NOT NULL
);
ALTER TABLE "CERTIFIKAT_PRILOGA_CLAN"
ADD CONSTRAINT "FK_CERTIFIKAT_PRILOGA_CLAN_1"
   FOREIGN KEY (ID_CERTIFIKAT)
   REFERENCES "CERTIFIKAT" (ID);

ALTER TABLE CERTIFIKAT_PRILOGA_CLAN ADD (CONSTRAINT CERTIFIKAT_PRILOGA_CLAN_pk PRIMARY KEY (ID));

CREATE SEQUENCE CERTIFIKAT_PRILOGA_CLAN_seq START WITH 1;
-------------------------------------------------------------------------------------
CREATE TABLE "CERTIFIKAT_DEJAVNOST"
(
	ID NUMBER(10) NOT NULL,
	ID_CERTIFIKAT NUMBER NOT NULL,
	ID_DEJAVNOST NUMBER NOT NULL
);

ALTER TABLE "CERTIFIKAT_DEJAVNOST"
ADD CONSTRAINT "FK_DEJAVNOSTDEJAVNOST_2"
   FOREIGN KEY (ID_DEJAVNOST)
   REFERENCES "DEJAVNOST" (ID);

ALTER TABLE "CERTIFIKAT_PRILOGA_CLAN"
ADD CONSTRAINT "FK_CERTIFIKAT_PRILOGA_CLAN_2"
   FOREIGN KEY (ID_PRILOGA)
   REFERENCES "PRILOGA" (ID);

ALTER TABLE CERTIFIKAT_DEJAVNOST ADD (CONSTRAINT CERTIFIKAT_DEJAVNOST_pk PRIMARY KEY (ID));

CREATE SEQUENCE CERTIFIKAT_DEJAVNOST_seq START WITH 1;
-------------------------------------------------------------------------------------
CREATE TABLE "CERTIFIKAT_PRILOGA_PROIZVOD"
(
	ID NUMBER(10) NOT NULL,
	ID_CERTIFIKAT NUMBER NOT NULL,
	ID_PRILOGA NUMBER NOT NULL
);

ALTER TABLE "CERTIFIKAT_PRILOGA_PROIZVOD"
ADD CONSTRAINT "FK_CERT_PRILOGA_PROIZVOD_1"
   FOREIGN KEY (ID_CERTIFIKAT)
   REFERENCES "CERTIFIKAT" (ID);

ALTER TABLE "CERTIFIKAT_PRILOGA_PROIZVOD"
ADD CONSTRAINT "FK_CERT_PRILOGA_PROIZVOD_2"
   FOREIGN KEY (ID_PRILOGA)
   REFERENCES "PRILOGA" (ID);

ALTER TABLE CERTIFIKAT_PRILOGA_PROIZVOD ADD (CONSTRAINT CERT_PRILOGA_PROIZVOD_pk PRIMARY KEY (ID));

CREATE SEQUENCE CERT_PRILOGA_PROIZVOD_seq START WITH 1;
------------------------------------------------------------------------------------------------------------
CREATE TABLE "CERTIFIKAT_PROIZVOD"
(
	ID NUMBER(10) NOT NULL,
	ID_CERTIFIKAT NUMBER NOT NULL,
	ID_PROIZVOD NUMBER NOT NULL
);

ALTER TABLE "CERTIFIKAT_PROIZVOD"
ADD CONSTRAINT "FK_CERTIFIKAT_PROIZVOD_2"
   FOREIGN KEY (ID_PROIZVOD)
   REFERENCES "PROIZVOD" (ID);

ALTER TABLE CERTIFIKAT_PROIZVOD ADD (CONSTRAINT CERTIFIKAT_PROIZVOD_pk PRIMARY KEY (ID));

CREATE SEQUENCE CERTIFIKAT_PROIZVOD_seq START WITH 1;
------------------------------------------------------------------------------------------------------------
CREATE TABLE "PRILOGA_CLAN"
(
	ID NUMBER(10) NOT NULL,
	ID_PRILOGA NUMBER NOT NULL,
	ID_SUBJEKT NUMBER NOT NULL
);

ALTER TABLE "PRILOGA_CLAN"
ADD CONSTRAINT "FK_PRILOGA_CLAN_1"
   FOREIGN KEY (ID_PRILOGA)
   REFERENCES "PRILOGA" (ID);

ALTER TABLE "PRILOGA_CLAN"
ADD CONSTRAINT "FK_PRILOGA_CLAN_2"
   FOREIGN KEY (ID_SUBJEKT)
   REFERENCES "SUBJEKT" (ID);

ALTER TABLE PRILOGA_CLAN ADD (CONSTRAINT PRILOGA_CLAN_pk PRIMARY KEY (ID));

CREATE SEQUENCE PRILOGA_CLAN_seq START WITH 1;
------------------------------------------------------------------------------------------------------------

ALTER TABLE "SHEMA"
ADD CONSTRAINT UC_SHEMA UNIQUE (naziv);

ALTER TABLE "ZASCITENPROIZVOD"
ADD CONSTRAINT UC_ZASCITENPROIZVOD UNIQUE (id_shema, naziv);

ALTER TABLE "PROIZVOD"
ADD CONSTRAINT UC_PROIZVOD UNIQUE (id_zasciten_proizvod, naziv);

ALTER TABLE "CERTIFIKAT_PROIZVOD"
ADD CONSTRAINT UC_CERTIFIKAT_PROIZVOD UNIQUE (ID_PROIZVOD, ID_CERTIFIKAT);

ALTER TABLE "PRILOGA_CLAN"
ADD CONSTRAINT UC_PRILOGA_CLAN UNIQUE (ID_PRILOGA, ID_SUBJEKT);

ALTER TABLE "ZASCITNIZNAK_PROIZVOD"
ADD CONSTRAINT UC_ZASCITNIZNAK_PROIZVOD UNIQUE (ID_ZASCITNIZNAK, ID_PROIZVOD);

ALTER TABLE "CERTIFIKAT_PRILOGA_PROIZVOD"
ADD CONSTRAINT UC_CERT_PRILOGA_PROIZVOD UNIQUE (ID_CERTIFIKAT, ID_PRILOGA);

ALTER TABLE "CERTIFIKAT_PRILOGA_CLAN"
ADD CONSTRAINT UC_CERTIFIKAT_PRILOGA_CLAN UNIQUE (ID_CERTIFIKAT, ID_PRILOGA);

ALTER TABLE "SUBJEKT"
ADD CONSTRAINT UC_SUBJEKT UNIQUE (ID_SUBJ, DAT_ZS);

create index ix_certifikat_dat_kontrole on certifikat ( dat_kontrole );
create index ix_certifikat_dat_velj on certifikat ( dat_velj );
create index ix_certifikat_dat_izdaje on certifikat ( dat_izdaje );


create index ix_subjekt_kmgmid on subjekt ( kmgmid );
create index ix_subjekt_maticna on subjekt ( maticna );
create index ix_subjekt_davcna on subjekt ( davcna );
create index ix_subjekt_naziv on subjekt ( naziv );

CREATE TABLE "KO"
(
	ID NUMBER(10) NOT NULL,
	"SIF" VARCHAR2(2000 BYTE) NOT NULL,
	"NAZIV" VARCHAR2(2000 BYTE) NOT NULL,
	"NASLOV" VARCHAR2(2000 BYTE) NOT NULL
);

insert into "KO" (id, sif, naziv, naslov) values (1, 'BUREAU','Bureau Veritas d.o.o.', 'Linhartova 49a, 1000 Ljubljana');
insert into "KO" (id, sif, naziv, naslov) values (2, 'KONCERT','In??titut za kontrolo in certifikacijo v kmetijstvu in gozdarstvu', 'Vinarska u. 14, 2000 Maribor');
insert into "KO" (id, sif, naziv, naslov) values (3, 'IKC','In??titut za kontrolo in certificiranje Univerze v Mariboru','Pivola 8, 2311 Ho??e');

ALTER TABLE "CERTIFIKAT" ADD id_parent number null;

CREATE TABLE "CERTIFIKAT_EXT"
(
	ID NUMBER(10) NOT NULL,
	"ID_CERTIFIKAT" NUMBER NOT NULL,
    "ID_IMIS" varchar2(2000) NOT NULL
);

ALTER TABLE "CERTIFIKAT_EXT"
ADD CONSTRAINT "FK_CERTIFIKATEXT_1"
   FOREIGN KEY (ID_CERTIFIKAT)
   REFERENCES "CERTIFIKAT" (ID);
   
ALTER TABLE "CERTIFIKAT_EXT"
ADD CONSTRAINT UC_IDCERTIFIKAT UNIQUE (id_certifikat);

CREATE SEQUENCE CERTIFIKAT_EXT_seq START WITH 1;

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

create or replace view evReportWithMebers as
select a.id_subjekt, a.id_zasciten_proizvod, a.leto, a.id_shema
from (
    select 
        c.id_imetnik as id_subjekt, c.id_zasciten_proizvod, EXTRACT(YEAR from c.dat_izdaje) as leto, zp.id_shema
    from 
        "CERTIFIKAT" c
        inner join "ZASCITENPROIZVOD" zp on c.id_zasciten_proizvod = zp.id
    where 
        c.status in ('Veljaven', 'Neveljaven', 'Arhiv')
    union
    select 
        p.id_subjekt, c.id_zasciten_proizvod, EXTRACT(YEAR from c.dat_izdaje) as leto, zp.id_shema
    from 
        "CERTIFIKAT" c
        inner join "ZASCITENPROIZVOD" zp on c.id_zasciten_proizvod = zp.id
        inner join "CERTIFIKAT_PRILOGA_CLAN" cp on c.id = cp.id_certifikat
        inner join "PRILOGA_CLAN" p on cp.id_priloga = p.id_priloga
    where 
        c.status in ('Veljaven', 'Neveljaven', 'Arhiv')
)a 
group by 
    a.id_subjekt, a.id_zasciten_proizvod, a.leto, a.id_shema
/

ALTER TABLE PROIZVOD 
ADD NEAKTIVEN NUMBER(1) DEFAULT 0 NOT NULL;
/
ALTER TABLE DEJAVNOST 
ADD NEAKTIVEN NUMBER(1) DEFAULT 0 NOT NULL;
/
ALTER TABLE ZAKONSKAPODLAGA 
ADD NEAKTIVEN NUMBER(1) DEFAULT 0 NOT NULL;
/
ALTER TABLE ZASCITNIZNAK 
ADD NEAKTIVEN NUMBER(1) DEFAULT 0 NOT NULL;
/

CREATE TABLE "NASTAVITVE" 
(	
	ID NUMBER(10) NOT NULL,
	VERZIJA VARCHAR2(25) NOT NULL
);

ALTER TABLE NASTAVITVE ADD (CONSTRAINT NASTAVITVE_pk PRIMARY KEY (ID));

CREATE SEQUENCE NASTAVITVE_seq START WITH 1;
------------------------------------------------------------------------------------------------------------

alter table zascitniznak
add stevilka varchar2(25) null;
/
update zascitniznak set stevilka = rownum;
/
alter table zascitniznak
modify stevilka varchar2(25) not null;
/
alter table proizvod
add enota varchar2(25) null;
/
update proizvod set enota = 'KOM';
/
alter table proizvod
modify enota varchar2(25) not null;
/
-- TODO
create or replace view evSubjekt as
select 
      (ROWNUM * -1) as id
      ,S.subj_id
      ,TO_CHAR(N.KMG_MID) as KMGMID -- lahko je null
      ,NVL(S.NAZIV, S.IME || ' ' || S.PRIIMEK) as NAZIV
      ,S.IME
      ,S.PRIIMEK
      ,S.HS_MID
      ,S.NASLOV
      ,S.OB_ID
      ,S.OBCINA
      ,S.POSTNA_ST AS ID_POSTE
      ,S.POSTA
      ,S.DS AS DAVCNA
      ,S.MSO AS MATICNA
      ,S.TEL_ST
      ,S.EMAIL
      ,NVL(case when MAX.dat_ZS > N.dat_zs then MAX.dat_zs else N.dat_zs end , TO_DATE('1900/01/01 00:00:00', 'yyyy/mm/dd hh24:mi:ss')) as DAT_ZS
      ,NVL(S.status, N.status) as status -- N = neveljaven
from 
    APP_ADMIN.ESK_SUBJ S
    inner join (select subj_id, max(dat_zs) as dat_zs from APP_ADMIN.ESK_SUBJ group by subj_id) max on S.subj_id = max.subj_id and S.dat_zs = max.dat_zs
    left join APP_ADMIN.ESK_KMG_NOSILEC N ON s.subj_id = N.subj_id_nosilec
where
    NVL(S.status, N.status) != 'N'
/
-- TODO
alter view evSubjekt add constraint PK_EVSUBJEKT primary key (subj_id, dat_zs) disable;

ALTER TABLE PROIZVODKOLICINE
ADD KMGMID varchar2(25) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD NAZIV_SUBJ varchar2(250) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD NASLOV varchar2(250) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD ID_POSTE varchar2(25) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD POSTA varchar2(250) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD DAVCNA varchar2(25) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD MATICNA varchar2(25) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD TEL_ST varchar2(25) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD EMAIL varchar2(250) null;
/
ALTER TABLE PROIZVODKOLICINE
DROP COLUMN "ID_PROIZVOD";
/
ALTER TABLE PROIZVODKOLICINE
ADD SHEMA varchar2(250) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD ZASCITENPROIZVOD varchar2(250) null;
/
ALTER TABLE PROIZVODKOLICINE
ADD PROIZVOD varchar2(250) null;
/