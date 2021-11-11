package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "PROIZVODKOLICINE")
public class ProizvodKolicine {
    private int id;
    private Double vrednost;
    private String enota;
    private String naslov;
    private long leto;
    private String kmgmid;
    private String nazivSubj;
    private String idPoste;
    private String posta;
    private String davcna;
    private String maticna;
    private String telSt;
    private String email;
    private String shema;
    private String zascitenproizvod;
    private String proizvod;
    private Long spremenil;
    private Date datSpremembe;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROIZVODKOLICINE_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.PROIZVODKOLICINE_seq", allocationSize = 1, name = "PROIZVODKOLICINE_seq")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Basic
    @Column(name = "VREDNOST", nullable = true, precision = 0)
    public Double getVrednost() {
        return vrednost;
    }

    public void setVrednost(Double vrednost) {
        this.vrednost = vrednost;
    }

    @Basic
    @Column(name = "ENOTA", nullable = true, length = 2000)
    public String getEnota() {
        return enota;
    }

    public void setEnota(String enota) {
        this.enota = enota;
    }

    @Basic
    @Column(name = "NASLOV", nullable = true, length = 250)
    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    @Basic
    @Column(name = "LETO", nullable = false, precision = 0)
    public long getLeto() {
        return leto;
    }

    public void setLeto(long leto) {
        this.leto = leto;
    }

    @Basic
    @Column(name = "KMGMID", nullable = true, length = 25)
    public String getKmgmid() {
        return kmgmid;
    }

    public void setKmgmid(String kmgmid) {
        this.kmgmid = kmgmid;
    }

    @Basic
    @Column(name = "NAZIV_SUBJ", nullable = true, length = 250)
    public String getNazivSubj() {
        return nazivSubj;
    }

    public void setNazivSubj(String nazivSubj) {
        this.nazivSubj = nazivSubj;
    }

    @Basic
    @Column(name = "ID_POSTE", nullable = true, length = 25)
    public String getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(String idPoste) {
        this.idPoste = idPoste;
    }

    @Basic
    @Column(name = "POSTA", nullable = true, length = 250)
    public String getPosta() {
        return posta;
    }

    public void setPosta(String posta) {
        this.posta = posta;
    }

    @Basic
    @Column(name = "DAVCNA", nullable = true, length = 25)
    public String getDavcna() {
        return davcna;
    }

    public void setDavcna(String davcna) {
        this.davcna = davcna;
    }

    @Basic
    @Column(name = "MATICNA", nullable = true, length = 25)
    public String getMaticna() {
        return maticna;
    }

    public void setMaticna(String maticna) {
        this.maticna = maticna;
    }

    @Basic
    @Column(name = "TEL_ST", nullable = true, length = 25)
    public String getTelSt() {
        return telSt;
    }

    public void setTelSt(String telSt) {
        this.telSt = telSt;
    }

    @Basic
    @Column(name = "EMAIL", nullable = true, length = 250)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "SHEMA", nullable = true, length = 250)
    public String getShema() {
        return shema;
    }

    public void setShema(String shema) {
        this.shema = shema;
    }

    @Basic
    @Column(name = "ZASCITENPROIZVOD", nullable = true, length = 250)
    public String getZascitenproizvod() {
        return zascitenproizvod;
    }

    public void setZascitenproizvod(String zascitenproizvod) {
        this.zascitenproizvod = zascitenproizvod;
    }

    @Basic
    @Column(name = "PROIZVOD", nullable = true, length = 250)
    public String getProizvod() {
        return proizvod;
    }

    public void setProizvod(String proizvod) {
        this.proizvod = proizvod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProizvodKolicine kolicine = (ProizvodKolicine) o;
        return id == kolicine.id &&
                leto == kolicine.leto &&
                Objects.equals(vrednost, kolicine.vrednost) &&
                Objects.equals(enota, kolicine.enota) &&
                Objects.equals(kmgmid, kolicine.kmgmid) &&
                Objects.equals(nazivSubj, kolicine.nazivSubj) &&
                Objects.equals(idPoste, kolicine.idPoste) &&
                Objects.equals(posta, kolicine.posta) &&
                Objects.equals(davcna, kolicine.davcna) &&
                Objects.equals(maticna, kolicine.maticna) &&
                Objects.equals(telSt, kolicine.telSt) &&
                Objects.equals(email, kolicine.email) &&
                Objects.equals(shema, kolicine.shema) &&
                Objects.equals(zascitenproizvod, kolicine.zascitenproizvod) &&
                Objects.equals(proizvod, kolicine.proizvod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vrednost, enota, leto, kmgmid, nazivSubj, idPoste, posta, davcna, maticna, telSt, email, shema, zascitenproizvod, proizvod);
    }

    @Basic
    @Column(name = "SPREMENIL", nullable = true, precision = 0)
    public Long getSpremenil() {
        return spremenil;
    }

    public void setSpremenil(Long spremenil) {
        this.spremenil = spremenil;
    }

    @Basic
    @Column(name = "DAT_SPREMEMBE", nullable = true)
    public Date getDatSpremembe() {
        return datSpremembe;
    }

    public void setDatSpremembe(Date datSpremembe) {
        this.datSpremembe = datSpremembe;
    }
}
