package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "ZASCITNIZNAK")
public class Zascitniznak {
    private long id;
    private String zzShema;
    private String nazivProizvoda;
    private Date datOdl;
    private String stOdl;
    private String certOrgan;
    private Subjekt imetnik;
    private boolean neaktiven;
    private String stevilka;
    private Long spremenil;
    private Time datSpremembe;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZASCITNIZNAK_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.ZASCITNIZNAK_seq", allocationSize = 1, name = "ZASCITNIZNAK_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ZZ_SHEMA", nullable = true, length = 2000)
    public String getZzShema() {
        return zzShema;
    }

    public void setZzShema(String zzShema) {
        this.zzShema = zzShema;
    }

    @Basic
    @Column(name = "NAZIV_PROIZVODA", nullable = true, length = 2000)
    public String getNazivProizvoda() {
        return nazivProizvoda;
    }

    public void setNazivProizvoda(String nazivProizvoda) {
        this.nazivProizvoda = nazivProizvoda;
    }

    @Basic
    @Column(name = "DAT_ODL", nullable = true)
    public Date getDatOdl() {
        return datOdl;
    }

    public void setDatOdl(Date datOdl) {
        this.datOdl = datOdl;
    }

    @Basic
    @Column(name = "ST_ODL", nullable = true, length = 2000)
    public String getStOdl() {
        return stOdl;
    }

    public void setStOdl(String stOdl) {
        this.stOdl = stOdl;
    }

    @Basic
    @Column(name = "CERT_ORGAN", nullable = true, length = 2000)
    public String getCertOrgan() {
        return certOrgan;
    }

    public void setCertOrgan(String certOrgan) {
        this.certOrgan = certOrgan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zascitniznak that = (Zascitniznak) o;
        return id == that.id &&
                Objects.equals(zzShema, that.zzShema) &&
                Objects.equals(nazivProizvoda, that.nazivProizvoda) &&
                Objects.equals(datOdl, that.datOdl) &&
                Objects.equals(stOdl, that.stOdl) &&
                Objects.equals(certOrgan, that.certOrgan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zzShema, nazivProizvoda, datOdl, stOdl, certOrgan);
    }

    @ManyToOne
    @JoinColumn(name = "ID_IMETNIK", referencedColumnName = "ID", nullable = false)
    public Subjekt getImetnik() {
        return imetnik;
    }

    public void setImetnik(Subjekt subjektByIdImetnik) {
        this.imetnik = subjektByIdImetnik;
    }

    @Basic
    @Column(name = "NEAKTIVEN", nullable = false, precision = 0)
    public boolean getNeaktiven() {
        return neaktiven;
    }

    public void setNeaktiven(boolean neaktiven) {
        this.neaktiven = neaktiven;
    }

    @Basic
    @Column(name = "STEVILKA", nullable = false, length = 25)
    public String getStevilka() {
        return stevilka;
    }

    public void setStevilka(String stevilka) {
        this.stevilka = stevilka;
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
    public Time getDatSpremembe() {
        return datSpremembe;
    }

    public void setDatSpremembe(Time datSpremembe) {
        this.datSpremembe = datSpremembe;
    }
}
