CREATE OR REPLACE VIEW ESK_DATA.CERT_VIEW AS
SELECT 
	c.id as id_certifikat, 
	c.stevilka, 
    c.dat_izdaje,
    c.dat_velj,
	s.naziv as shema, 
	zp.naziv as zasciten_proizvod, 
    p.naziv as proizvod,
	NVL(sub.naziv, sub.ime || ' ' || sub.priimek) as subjekt_naziv,
    sub.kmgmid,
    sub.maticna,
	sub.naslov as subjekt_naslov,
	sub.id_poste || ' ' || sub.posta as subjekt_kraj
FROM
    ESK_DATA.CERTIFIKAT c
    INNER JOIN ESK_DATA.ZASCITENPROIZVOD zp on c.id_zasciten_proizvod = zp.id
    INNER JOIN ESK_DATA.SHEMA s on zp.id_shema = s.id
    INNER JOIN ESK_DATA.SUBJEKT sub on c.id_imetnik = sub.id
    LEFT JOIN ESK_DATA.CERTIFIKAT_PROIZVOD cp on c.id = cp.id_certifikat
    LEFT JOIN ESK_DATA.PROIZVOD p on cp.id_proizvod = p.id
WHERE
    c.status = 'Veljaven'
/



CREATE OR REPLACE VIEW ESK_DATA.EVSUBJEKT ("ID", "SUBJ_ID", "KMGMID", "NAZIV", "IME", "PRIIMEK", "HS_MID", "NASLOV", "OB_ID", "OBCINA", "ID_POSTE", "POSTA", "DAVCNA", "MATICNA", "TEL_ST", "EMAIL", "DAT_ZS", "STATUS") AS 
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
      ,S.DAT_ZS
      ,NVL(N.status, S.status) as status -- N = neveljaven
  from 
	APP_ADMIN.ESK_SUBJ S, APP_ADMIN.ESK_KMG_NOSILEC N
  where
    s.subj_id = N.subj_id_nosilec AND
    NVL(N.status, S.status) != 'N';
/