package zaslontelecom.esk.backend.api.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class CertView {
    @Id
    long ID;
    String TIP;
    String STEVILKA;
    Date DAT_KONTROLE;
    Date DAT_IZDAJE;
    Date DAT_VELJ;
    String STATUS;
    String TEL_ST;
    String EMAIL;
    String OPOMBA;
    String DAT_VNOSA;
    String KONTROLOR;
    long id_uporabnik;
    long dej_id;
    String dej_naziv;
    long zp_id;
    String zp_naziv;
    long shema_id;
    String shema_naziv;
    long sub_id;
    String sub_KMGMID;
    String sub_IME;
    String sub_PRIIMEK;
    String sub_NAZIV;
    String sub_NASLOV;
    String sub_MATICNA;
    String sub_DAVCNA;
    String sub_ID_POSTE;
    String sub_POSTA;
    String sub_TEL_ST;
    String sub_EMAIL;
    String sub_ID_SUBJ;
    String sub_OB_ID;
    String sub_OBCINA;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTIP() {
        return TIP;
    }

    public void setTIP(String TIP) {
        this.TIP = TIP;
    }

    public String getSTEVILKA() {
        return STEVILKA;
    }

    public void setSTEVILKA(String STEVILKA) {
        this.STEVILKA = STEVILKA;
    }

    public Date getDAT_KONTROLE() {
        return DAT_KONTROLE;
    }

    public void setDAT_KONTROLE(Date DAT_KONTROLE) {
        this.DAT_KONTROLE = DAT_KONTROLE;
    }


    public Date getDAT_IZDAJE() {
        return DAT_IZDAJE;
    }

    public void setDAT_IZDAJE(Date DAT_IZDAJE) {
        this.DAT_IZDAJE = DAT_IZDAJE;
    }

    public Date getDAT_VELJ() {
        return DAT_VELJ;
    }

    public void setDAT_VELJ(Date DAT_VELJ) {
        this.DAT_VELJ = DAT_VELJ;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTEL_ST() {
        return TEL_ST;
    }

    public void setTEL_ST(String TEL_ST) {
        this.TEL_ST = TEL_ST;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getOPOMBA() {
        return OPOMBA;
    }

    public void setOPOMBA(String OPOMBA) {
        this.OPOMBA = OPOMBA;
    }

    public String getDAT_VNOSA() {
        return DAT_VNOSA;
    }

    public void setDAT_VNOSA(String DAT_VNOSA) {
        this.DAT_VNOSA = DAT_VNOSA;
    }

    public String getKONTROLOR() {
        return KONTROLOR;
    }

    public void setKONTROLOR(String KONTROLOR) {
        this.KONTROLOR = KONTROLOR;
    }

    public long getId_uporabnik() {
        return id_uporabnik;
    }

    public void setId_uporabnik(long id_uporabnik) {
        this.id_uporabnik = id_uporabnik;
    }

    public long getDej_id() {
        return dej_id;
    }

    public void setDej_id(long dej_id) {
        this.dej_id = dej_id;
    }

    public String getDej_naziv() {
        return dej_naziv;
    }

    public void setDej_naziv(String dej_naziv) {
        this.dej_naziv = dej_naziv;
    }

    public long getZp_id() {
        return zp_id;
    }

    public void setZp_id(long zp_id) {
        this.zp_id = zp_id;
    }

    public String getZp_naziv() {
        return zp_naziv;
    }

    public void setZp_naziv(String zp_naziv) {
        this.zp_naziv = zp_naziv;
    }

    public long getShema_id() {
        return shema_id;
    }

    public void setShema_id(long shema_id) {
        this.shema_id = shema_id;
    }

    public String getShema_naziv() {
        return shema_naziv;
    }

    public void setShema_naziv(String shema_naziv) {
        this.shema_naziv = shema_naziv;
    }

    public long getSub_id() {
        return sub_id;
    }

    public void setSub_id(long sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_KMGMID() {
        return sub_KMGMID;
    }

    public void setSub_KMGMID(String sub_KMGMID) {
        this.sub_KMGMID = sub_KMGMID;
    }

    public String getSub_IME() {
        return sub_IME;
    }

    public void setSub_IME(String sub_IME) {
        this.sub_IME = sub_IME;
    }

    public String getSub_PRIIMEK() {
        return sub_PRIIMEK;
    }

    public void setSub_PRIIMEK(String sub_PRIIMEK) {
        this.sub_PRIIMEK = sub_PRIIMEK;
    }

    public String getSub_NAZIV() {
        return sub_NAZIV;
    }

    public void setSub_NAZIV(String sub_NAZIV) {
        this.sub_NAZIV = sub_NAZIV;
    }

    public String getSub_NASLOV() {
        return sub_NASLOV;
    }

    public void setSub_NASLOV(String sub_NASLOV) {
        this.sub_NASLOV = sub_NASLOV;
    }

    public String getSub_MATICNA() {
        return sub_MATICNA;
    }

    public void setSub_MATICNA(String sub_MATICNA) {
        this.sub_MATICNA = sub_MATICNA;
    }

    public String getSub_DAVCNA() {
        return sub_DAVCNA;
    }

    public void setSub_DAVCNA(String sub_DAVCNA) {
        this.sub_DAVCNA = sub_DAVCNA;
    }

    public String getSub_ID_POSTE() {
        return sub_ID_POSTE;
    }

    public void setSub_ID_POSTE(String sub_ID_POSTE) {
        this.sub_ID_POSTE = sub_ID_POSTE;
    }

    public String getSub_POSTA() {
        return sub_POSTA;
    }

    public void setSub_POSTA(String sub_POSTA) {
        this.sub_POSTA = sub_POSTA;
    }

    public String getSub_TEL_ST() {
        return sub_TEL_ST;
    }

    public void setSub_TEL_ST(String sub_TEL_ST) {
        this.sub_TEL_ST = sub_TEL_ST;
    }

    public String getSub_EMAIL() {
        return sub_EMAIL;
    }

    public void setSub_EMAIL(String sub_EMAIL) {
        this.sub_EMAIL = sub_EMAIL;
    }

    public String getSub_ID_SUBJ() {
        return sub_ID_SUBJ;
    }

    public void setSub_ID_SUBJ(String sub_ID_SUBJ) {
        this.sub_ID_SUBJ = sub_ID_SUBJ;
    }

    public String getSub_OB_ID() {
        return sub_OB_ID;
    }

    public void setSub_OB_ID(String sub_OB_ID) {
        this.sub_OB_ID = sub_OB_ID;
    }

    public String getSub_OBCINA() {
        return sub_OBCINA;
    }

    public void setSub_OBCINA(String sub_OBCINA) {
        this.sub_OBCINA = sub_OBCINA;
    }
}
