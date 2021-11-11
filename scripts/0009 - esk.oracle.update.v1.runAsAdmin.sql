CREATE OR REPLACE FORCE VIEW ESK_DATA.CERT_VIEW
AS
   (SELECT c.id AS id_certifikat,
           CASE WHEN c.tip = 'I' THEN 'Individualen' ELSE 'Skupinski' END tip,
           c.stevilka AS STEVILKA_CERTIFIKATA,
           c.organizacija AS kontrolna_organizacija,
           TO_CHAR (c.dat_izdaje, 'dd.mm.yyyy') AS datum_izdaje,
           TO_CHAR (c.dat_velj, 'dd.mm.yyyy') AS datum_veljavnosti_do,
           s.naziv AS shema,
           zp.naziv AS zasciten_proizvod,
           sub.id_subj AS imetnik_id_subj,
           sub.kmgmid imetnik_kmg_mid,
           sub.maticna imetnik_maticna,
           sub.davcna imetnik_davcna,
           clan.id_subj AS clan_id_subj,
           clan.kmgmid AS clan_kmg_mid,
           clan.maticna AS clan_maticna,
           clan.davcna AS clan_davcna,
           priloga.stevilka AS stevilka_priloge,
           TO_CHAR (priloga.dat_izdaje, 'dd.mm.yyyy') AS datum_izdaje_priloga,
           TO_CHAR (PRILOGA.DAT_VELJ, 'dd.mm.yyyy')
              AS datum_veljavnosti_do_priloga
      FROM ESK_DATA.CERTIFIKAT c
           INNER JOIN ESK_DATA.ZASCITENPROIZVOD zp
              ON c.id_zasciten_proizvod = zp.id
           INNER JOIN ESK_DATA.SHEMA s ON zp.id_shema = s.id
           INNER JOIN ESK_DATA.SUBJEKT sub ON c.id_imetnik = sub.id
           LEFT JOIN ESK_DATA.CERTIFIKAT_PROIZVOD cp
              ON c.id = cp.id_certifikat
           LEFT JOIN ESK_DATA.PROIZVOD p ON cp.id_proizvod = p.id
           LEFT JOIN ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp
              ON cpp.id_certifikat = c.id
           LEFT JOIN ESK_DATA.PRILOGA_CLAN pc
              ON cpp.id_priloga = pc.id_priloga
           LEFT JOIN ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id
           LEFT JOIN ESK_DATA.PRILOGA priloga ON pc.id_priloga = priloga.id);
/

GRANT SELECT ON ESK_DATA.CERT_VIEW TO AKTRP_PREGLED WITH GRANT OPTION;
/
GRANT SELECT ON ESK_DATA.CERT_VIEW TO APP_ADMIN WITH GRANT OPTION;
/
GRANT SELECT ON ESK_DATA.CERT_VIEW TO MKGP_APPS WITH GRANT OPTION;
/
GRANT SELECT ON ESK_DATA.CERT_VIEW TO UVHVVR_PREGLED WITH GRANT OPTION;
/
ALTER TABLE ESK_DATA.ko
ADD EXT_SIF VARCHAR2(200);
/
ALTER TABLE ESK_DATA.ko
ADD PODPISNIK VARCHAR2(200);
/
ALTER TABLE ESK_DATA.ko
ADD PODPISNIK_VLOGA VARCHAR2(200);
/
update ESK_DATA.ko set EXT_SIF ='BUREAU VERITAS', PODPISNIK='Tomaž Verbnik, univ.dipl.inž.', PODPISNIK_VLOGA='Vodja certifikacijskega organa:' WHERE SIF = 'BUREAU';
/
update ESK_DATA.ko set EXT_SIF ='KON-CERT', PODPISNIK='Robert Rojko, univ.dipl.ing.', PODPISNIK_VLOGA='direktor' WHERE SIF = 'KONCERT';
/
update ESK_DATA.ko set EXT_SIF ='IKC-UM', PODPISNIK='Katja Sabolič', PODPISNIK_VLOGA='direktorica' WHERE SIF = 'IKC';
/
alter table esk_data.proizvodkolicine
modify vrednost numeric(18,2);
/
alter table esk_data.proizvodkolicine_hist
modify vrednost numeric(18,2);
/