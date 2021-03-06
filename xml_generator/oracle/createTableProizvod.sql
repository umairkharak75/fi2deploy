CREATE TABLE "ESK_DATA"."PROIZVOD" 
(	
	ID NUMBER GENERATED ALWAYS AS IDENTITY,
	NAZIV VARCHAR2(2000),
	ID_SHEMA NUMBER NOT NULL,
	ID_ZAKONSKA_PODLAGA NUMBER NOT NULL,
	ID_ZASCITEN_PROIZVOD NUMBER NOT NULL,
	CONSTRAINT PK_PROIZVOD_ID PRIMARY KEY (ID)
);
ALTER TABLE "ESK_DATA"."PROIZVOD" 
ADD CONSTRAINT "FK_PROIZVODSHEMAID_SHEMA"
   FOREIGN KEY (ID_SHEMA)
   REFERENCES "SHEMA" (ID);

ALTER TABLE "ESK_DATA"."PROIZVOD" 
ADD CONSTRAINT "FK_PROIZVODZAKONSKAPODLAGAID_ZAKONSKA_PODLAGA"
   FOREIGN KEY (ID_ZAKONSKA_PODLAGA)
   REFERENCES "ZAKONSKAPODLAGA" (ID);

ALTER TABLE "ESK_DATA"."PROIZVOD" 
ADD CONSTRAINT "FK_PROIZVODZASCITENPROIZVODID_ZASCITEN_PROIZVOD"
   FOREIGN KEY (ID_ZASCITEN_PROIZVOD)
   REFERENCES "ZASCITENPROIZVOD" (ID);

