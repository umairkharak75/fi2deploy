package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "evsubjekt")
public class Evsubjekt {
    @Id
    private long id;
    private long subjId;
    private String kmgmid;
    private String naziv;
    private String ime;
    private String priimek;
    private String hsMid;
    private String naslov;
    private String obId;
    private String obcina;
    private String idPoste;
    private String posta;
    private String davcna;
    private String maticna;
    private String telSt;
    private String email;
    private Date datZs;
    private String status;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SUBJ_ID", nullable = false, precision = 0)
    public long getSubjId() {
        return subjId;
    }

    public void setSubjId(long subjId) {
        this.subjId = subjId;
    }

    @Basic
    @Column(name = "KMGMID", nullable = true, length = 100)
    public String getKmgmid() {
        return kmgmid;
    }

    public void setKmgmid(String kmgmid) {
        this.kmgmid = kmgmid;
    }

    @Basic
    @Column(name = "NAZIV", nullable = true, length = 200)
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Basic
    @Column(name = "IME", nullable = true, length = 2000)
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    @Basic
    @Column(name = "PRIIMEK", nullable = true, length = 2000)
    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    @Basic
    @Column(name = "HS_MID", nullable = true, length = 2000)
    public String getHsMid() {
        return hsMid;
    }

    public void setHsMid(String hsMid) {
        this.hsMid = hsMid;
    }

    @Basic
    @Column(name = "NASLOV", nullable = true, length = 2000)
    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    @Basic
    @Column(name = "OB_ID", nullable = true, length = 2000)
    public String getObId() {
        return obId;
    }

    public void setObId(String obId) {
        this.obId = obId;
    }

    @Basic
    @Column(name = "OBCINA", nullable = true, length = 2000)
    public String getObcina() {
        return obcina;
    }

    public void setObcina(String obcina) {
        this.obcina = obcina;
    }

    @Basic
    @Column(name = "ID_POSTE", nullable = true, length = 2000)
    public String getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(String idPoste) {
        this.idPoste = idPoste;
    }

    @Basic
    @Column(name = "POSTA", nullable = true, length = 2000)
    public String getPosta() {
        return posta;
    }

    public void setPosta(String posta) {
        this.posta = posta;
    }

    @Basic
    @Column(name = "DAVCNA", nullable = true, length = 2000)
    public String getDavcna() {
        return davcna;
    }

    public void setDavcna(String davcna) {
        this.davcna = davcna;
    }

    @Basic
    @Column(name = "MATICNA", nullable = true, length = 2000)
    public String getMaticna() {
        return maticna;
    }

    public void setMaticna(String maticna) {
        this.maticna = maticna;
    }

    @Basic
    @Column(name = "TEL_ST", nullable = true, length = 2000)
    public String getTelSt() {
        return telSt;
    }

    public void setTelSt(String telSt) {
        this.telSt = telSt;
    }

    @Basic
    @Column(name = "EMAIL", nullable = true, length = 2000)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "DAT_ZS", nullable = true)
    public Date getDatZs() {
        return datZs;
    }

    public void setDatZs(Date datZs) {
        this.datZs = datZs;
    }

    @Basic
    @Column(name = "STATUS", nullable = true, length = 1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evsubjekt evsubjekt = (Evsubjekt) o;
        return subjId == evsubjekt.subjId &&
                Objects.equals(kmgmid, evsubjekt.kmgmid) &&
                Objects.equals(naziv, evsubjekt.naziv) &&
                Objects.equals(ime, evsubjekt.ime) &&
                Objects.equals(priimek, evsubjekt.priimek) &&
                Objects.equals(hsMid, evsubjekt.hsMid) &&
                Objects.equals(naslov, evsubjekt.naslov) &&
                Objects.equals(obId, evsubjekt.obId) &&
                Objects.equals(obcina, evsubjekt.obcina) &&
                Objects.equals(idPoste, evsubjekt.idPoste) &&
                Objects.equals(posta, evsubjekt.posta) &&
                Objects.equals(davcna, evsubjekt.davcna) &&
                Objects.equals(maticna, evsubjekt.maticna) &&
                Objects.equals(telSt, evsubjekt.telSt) &&
                Objects.equals(email, evsubjekt.email) &&
                Objects.equals(datZs, evsubjekt.datZs) &&
                Objects.equals(status, evsubjekt.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjId, kmgmid, naziv, ime, priimek, hsMid, naslov, obId, obcina, idPoste, posta, davcna, maticna, telSt, email, datZs, status);
    }
}
