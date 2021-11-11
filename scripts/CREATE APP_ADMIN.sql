alter session set "_ORACLE_SCRIPT"=true;  
CREATE USER APP_ADMIN IDENTIFIED BY gtz7p32j;
GRANT CONNECT, RESOURCE, DBA TO APP_ADMIN;
GRANT CREATE SESSION TO APP_ADMIN; 
GRANT UNLIMITED TABLESPACE TO APP_ADMIN;

CREATE TABLE APP_ADMIN.ESK_SUBJ(
	ID NUMBER GENERATED ALWAYS AS IDENTITY,
    subj_id NUMBER NOT NULL,
    naziv VARCHAR2(2000) NULL,
	ime VARCHAR2(2000) NULL,
	priimek VARCHAR2(2000) NULL,
	Unaziv VARCHAR2(2000) NULL,
	HS_MID VARCHAR2(2000) NULL,
	NASLOV VARCHAR2(2000),
	ob_id VARCHAR2(2000) NULL,
	obcina VARCHAR2(2000) NULL,
	postna_st VARCHAR2(2000),
	posta VARCHAR2(2000),
	ds VARCHAR2(2000) NULL,
	MSO VARCHAR2(2000) NULL,
    tel_st VARCHAR2(2000) NULL,
    email VARCHAR2(2000) NULL,
    dat_zs DATE NOT NULL,
	status VARCHAR(1)
);
/

 CREATE TABLE APP_ADMIN.ESK_KMG_NOSILEC(
	ID NUMBER GENERATED ALWAYS AS IDENTITY,
    kmgmid VARCHAR2(100) NOT NULL,
    subj_id_nosilec NUMBER NOT NULL,
	status VARCHAR2(1) NOT NULL,
    dat_zs DATE NOT NULL
);
/

GRANT
  SELECT,
  INSERT,
  UPDATE,
  DELETE
ON
  APP_ADMIN.ESK_SUBJ
TO
  ESK_DATA;
  
GRANT
  SELECT,
  INSERT,
  UPDATE,
  DELETE
ON
  APP_ADMIN.ESK_KMG_NOSILEC
TO
  ESK_DATA;
  
GRANT
  SELECT,
  INSERT,
  UPDATE,
  DELETE
ON
  APP_ADMIN.ESK_USERS
TO
  ESK_DATA;
-----------------------------
-- View za (bruto) površine Intenzivnih sadovnjakov po sadnih vrstah - tekoče stanje

/*create or replace view APP_ADMIN.ESK_KMG_ISAD_AREA as
select K.kmg_mid
           ,(select subj_id_nosilec from rkg_kmg where kmg_mid = K.kmg_mid) SUBJ_ID_NOSILEC
           ,K.SUM_POV_VRSTA
           , K.vrsta_id
           , V.NAZIV
 from TNS.KMG_ISAD_VRSTE_POV K, tns.tns_vrste V
where V.vrsta_id = K.vrsta_id;*/


insert into  APP_ADMIN.ESK_SUBJ(subj_id, ime, priimek, naziv, naslov, MSO, DS, POSTNA_ST, posta, tel_st, email, unaziv, HS_MID, ob_id, obcina, dat_zs, status) values (1, 'Jure', 'Časl', null, 'Savinjska cesta 150', '2605980500299', null, '3313', 'Polzela', null, 'jure.casl', null, 'HS101', 3313, 'Polzela', sysdate, 'S');
insert into  APP_ADMIN.ESK_SUBJ(subj_id, ime, priimek, naziv, naslov, MSO, DS, POSTNA_ST, posta, tel_st, email, unaziv, HS_MID, ob_id, obcina, dat_zs, status) values (2, null, null, 'Mercator d.o.o.', 'Brnčičeva ulica 13', '57785689', '44485689', '1000', 'Ljubljana', '01/175 47 42', 'info.mercator', null, 'HS102', 1000, 'Ljubljana', sysdate, 'S');
insert into APP_ADMIN.ESK_SUBJ(subj_id, ime, priimek, naziv, naslov, MSO, DS, POSTNA_ST, posta, tel_st, email, unaziv, HS_MID, ob_id, obcina, dat_zs, status) values (3, null, null, 'Janko Petek s.p.', 'Pod klancem 12', null, '789987789', '5000', 'Koper', null, null, null, 'HS103', 5000, 'Koper', sysdate, 'S');
insert into  APP_ADMIN.ESK_SUBJ(subj_id, ime, priimek, naziv, naslov, MSO, DS, POSTNA_ST, posta, tel_st, email, unaziv, HS_MID, ob_id, obcina, dat_zs, status) values (4, null, null, 'Zadruga Šaleška dolina', 'Koroška cesta 107', '222333444', '789987789', '2000', 'Maribor', '02/222 55 22', 'info.zadruga', null, 'HS104', 2000, 'Maribor', sysdate, 'S');
insert into  APP_ADMIN.ESK_SUBJ(subj_id, ime, priimek, naziv, naslov, MSO, DS, POSTNA_ST, posta, tel_st, email, unaziv, HS_MID, ob_id, obcina, dat_zs, status) values (5, 'Janez', 'Novak', null, 'Pod trasami 4b', '2605980500299', null, '4000', 'Kranj', null, null, null, 'HS105', 4000, 'Kranj', sysdate, 'S');

insert into  APP_ADMIN.ESK_KMG_NOSILEC(kmgmid, subj_id_nosilec, status, dat_zs) values ('KMG-101', 1, 'S', sysdate);
insert into  APP_ADMIN.ESK_KMG_NOSILEC(kmgmid, subj_id_nosilec, status, dat_zs) values ('KMG-103', 3, 'S', sysdate);
insert into  APP_ADMIN.ESK_KMG_NOSILEC(kmgmid, subj_id_nosilec, status, dat_zs) values ('KMG-105', 4, 'S', sysdate);

CREATE TABLE "APP_ADMIN"."ESK_USERS"
(	
	"ORG_SIF" VARCHAR2(30 BYTE), 
	"ORG_IME" VARCHAR2(140 BYTE) NOT NULL, 
	"ORG_NASLOV" VARCHAR2(200 BYTE), 
	"USER_ID" NUMBER(5,0) NOT NULL, 
	"USER_NAME" VARCHAR2(30 BYTE) NOT NULL, 
	"DELAVEC_IME" VARCHAR2(30 BYTE) NOT NULL, 
	"MAIL" VARCHAR2(100 BYTE), 
	"TEL" VARCHAR2(30 BYTE)
);

insert into APP_ADMIN.ESK_USERS (ORG_SIF, ORG_IME, ORG_NASLOV, USER_ID, USER_NAME, DELAVEC_IME, MAIL, TEL) 
values ('BUREAU','Bureau Veritas d.o.o.','Linhartova 49a, 1000 Ljubljana',1,'jcasl','Jure Časl','jure@gmail.com','041212121');
insert into APP_ADMIN.ESK_USERS (ORG_SIF, ORG_IME, ORG_NASLOV, USER_ID, USER_NAME, DELAVEC_IME, MAIL, TEL) 
values ('BUREAU','Inštitut za kontrolo in certifikacijo v kmetijstvu in gozdarstvu','Vinarska u. 14, 2000 Maribor', 2,'rpirkovic','Robert Pirkovič','robert@gmail.com','041222321');
insert into APP_ADMIN.ESK_USERS (ORG_SIF, ORG_IME, ORG_NASLOV, USER_ID, USER_NAME, DELAVEC_IME, MAIL, TEL) 
values ('BUREAU','Inštitut za kontrolo in certificiranje Univerze v Mariboru','Pivola 8, 2311 Hoče',3,'astrekelj','Aleš Štrekelj','ales@gmail.com','0413123210');
insert into APP_ADMIN.ESK_USERS (ORG_SIF, ORG_IME, ORG_NASLOV, USER_ID, USER_NAME, DELAVEC_IME, MAIL, TEL) 
values ('BUREAU','Bureau Veritas d.o.o.','Pivola 8, 2311 Hoče',3,'vgrasek','Vlasta Grašek','vlasta@gmail.com','031333330');

commit;
