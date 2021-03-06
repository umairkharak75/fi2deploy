alter session set "_ORACLE_SCRIPT"=true;  
CREATE USER ESK_DATA IDENTIFIED BY gtz7p32j;
GRANT CONNECT, RESOURCE, DBA TO ESK_DATA;
GRANT CREATE SESSION TO ESK_DATA; 
GRANT UNLIMITED TABLESPACE TO ESK_DATA

GRANT SELECT ON APP_ADMIN.ESK_SUBJ TO ESK_DATA;
GRANT SELECT ON APP_ADMIN.ESK_KMG_NOSILEC TO ESK_DATA;
GRANT SELECT ON APP_ADMIN.ESK_USERS TO ESK_DATA;

alter session set "_ORACLE_SCRIPT"=true;  
CREATE USER APP_ESK_N IDENTIFIED BY gtz7p32j;
GRANT CONNECT, RESOURCE, DBA TO APP_ESK_N;
GRANT CREATE SESSION TO APP_ESK_N; 
GRANT UNLIMITED TABLESPACE TO APP_ESK_N;

GRANT SELECT ON APP_ADMIN.ESK_SUBJ TO APP_ESK_N;
GRANT SELECT ON APP_ADMIN.ESK_KMG_NOSILEC TO APP_ESK_N;
GRANT SELECT ON APP_ADMIN.ESK_USERS TO APP_ESK_N;

alter session set "_ORACLE_SCRIPT"=true;  
CREATE USER APP_ESK_Z IDENTIFIED BY gtz7p32j;
GRANT CONNECT, RESOURCE, DBA TO APP_ESK_Z;
GRANT CREATE SESSION TO APP_ESK_Z; 
GRANT UNLIMITED TABLESPACE TO APP_ESK_Z;