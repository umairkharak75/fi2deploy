CREATE TABLE "ESK_DATA"."CERTIFIKAT_PROIZVOD" 
(	
	ID NUMBER GENERATED ALWAYS AS IDENTITY,
	ID_CERTIFIKAT NUMBER,
	ID_PROIZVOD NUMBER,
	CONSTRAINT PK_CERTIFIKAT_PROIZVOD_ID PRIMARY KEY (ID)
);
ALTER TABLE "ESK_DATA"."CERTIFIKAT_PROIZVOD" 
ADD CONSTRAINT "FK_CERTIFIKAT_PROIZVODCERTIFIKATID_CERTIFIKAT"
   FOREIGN KEY (ID_CERTIFIKAT)
   REFERENCES "CERTIFIKAT" (ID);

ALTER TABLE "ESK_DATA"."CERTIFIKAT_PROIZVOD" 
ADD CONSTRAINT "FK_CERTIFIKAT_PROIZVODPROIZVODID_PROIZVOD"
   FOREIGN KEY (ID_PROIZVOD)
   REFERENCES "PROIZVOD" (ID);

