package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "SUBJEKT")
public class Subjekt {
    private long id;
    private String kmgmid;
    private String ime;
    private String priimek;
    private String naziv;
    private String naslov;
    private String maticna;
    private String davcna;
    private String idPoste;
    private String posta;
    private String telSt;
    private String email;
    private String obId;
    private String obcina;
    private long subjId;
    private Date datZs;
    private Long spremenil;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBJEKT_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.SUBJEKT_seq", allocationSize = 1, name = "SUBJEKT_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "KMGMID", nullable = true, length = 2000)
    public String getKmgmid() {
        return kmgmid;
    }

    public void setKmgmid(String kmgmid) {
        this.kmgmid = kmgmid;
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
    @Column(name = "NAZIV", nullable = true, length = 2000)
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
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
    @Column(name = "MATICNA", nullable = true, length = 2000)
    public String getMaticna() {
        return maticna;
    }

    public void setMaticna(String maticna) {
        this.maticna = maticna;
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
    @Column(name = "OB_ID", nullable = false, length = 2000)
    public String getObId() {
        return obId;
    }

    public void setObId(String obId) {
        this.obId = obId;
    }

    @Basic
    @Column(name = "OBCINA", nullable = false, length = 2000)
    public String getObcina() {
        return obcina;
    }

    public void setObcina(String obcina) {
        this.obcina = obcina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subjekt subjekt = (Subjekt) o;
        return id == subjekt.id &&
                Objects.equals(kmgmid, subjekt.kmgmid) &&
                Objects.equals(ime, subjekt.ime) &&
                Objects.equals(priimek, subjekt.priimek) &&
                Objects.equals(naziv, subjekt.naziv) &&
                Objects.equals(naslov, subjekt.naslov) &&
                Objects.equals(maticna, subjekt.maticna) &&
                Objects.equals(davcna, subjekt.davcna) &&
                Objects.equals(idPoste, subjekt.idPoste) &&
                Objects.equals(posta, subjekt.posta) &&
                Objects.equals(telSt, subjekt.telSt) &&
                Objects.equals(email, subjekt.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kmgmid, ime, priimek, naziv, naslov, maticna, davcna, idPoste, posta, telSt, email);
    }

    @Basic
    @Column(name = "DAT_ZS", length = 2000)
    public Date getDatZs() {
        return datZs;
    }

    public void setDatZs(Date datZs) {
        this.datZs = datZs;
    }

    @Basic
    @Column(name = "ID_SUBJ", nullable = false, length = 2000)
    public long getSubjId() {
        return subjId;
    }

    public void setSubjId(long subjId) {
        this.subjId = subjId;
    }

    @Basic
    @Column(name = "SPREMENIL", nullable = true, precision = 0)
    public Long getSpremenil() {
        return spremenil;
    }

    public void setSpremenil(Long spremenil) {
        this.spremenil = spremenil;
    }
}
