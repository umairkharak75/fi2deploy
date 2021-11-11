-- TODO
create or replace view evSubjektAll as
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
/
-- TODO
alter view evSubjektAll add constraint PK_EVSUBJEKTALL primary key (subj_id, dat_zs) disable;
