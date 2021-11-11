CREATE TABLE "ESK_DATA"."PRILOGA" 
(	
	ID NUMBER GENERATED ALWAYS AS IDENTITY,
	STEVILKA VARCHAR2(2000),
	DAT_IZDAJE DATE,
	DAT_VELJ DATE,
	STATUS VARCHAR2(2000),
	VSEBINA CLOB NULL,
	CONSTRAINT PK_PRILOGA_ID PRIMARY KEY (ID)
);
