package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "KO")
public class Ko {
    private long id;
    private String sif;
    private String naziv;
    private String naslov;
    private String extSif;
    private String podpisnik;
    private String podpisnikVloga;

    @Basic
    @Id
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SIF", nullable = false, length = 2000)
    public String getSif() {
        return sif;
    }

    public void setSif(String sif) {
        this.sif = sif;
    }

    @Basic
    @Column(name = "NAZIV", nullable = false, length = 2000)
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Basic
    @Column(name = "NASLOV", nullable = false, length = 2000)
    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ko ko = (Ko) o;
        return id == ko.id &&
                Objects.equals(sif, ko.sif) &&
                Objects.equals(naziv, ko.naziv) &&
                Objects.equals(naslov, ko.naslov);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sif, naziv, naslov);
    }

    @Basic
    @Column(name = "EXT_SIF")
    public String getExtSif() {
        return extSif;
    }

    public void setExtSif(String extSif) {
        this.extSif = extSif;
    }

    @Basic
    @Column(name = "PODPISNIK")
    public String getPodpisnik() {
        return podpisnik;
    }

    public void setPodpisnik(String podpisnik) {
        this.podpisnik = podpisnik;
    }

    @Basic
    @Column(name = "PODPISNIK_VLOGA")
    public String getPodpisnikVloga() {
        return podpisnikVloga;
    }

    public void setPodpisnikVloga(String podpisnikVloga) {
        this.podpisnikVloga = podpisnikVloga;
    }
}
