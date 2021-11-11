package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "PROIZVOD")
public class Proizvod {
    private long id;
    private String naziv;

    private ZakonskaPodlaga zakonskaPodlaga;
    private ZascitenProizvod zascitenProizvod;

    private Collection<ZascitniznakProizvod> zascitniznakProizvod;
    private boolean neaktiven;
    private String enota;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROIZVOD_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.PROIZVOD_seq", allocationSize = 1, name = "PROIZVOD_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAZIV", nullable = true, length = 2000)
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proizvod proizvod = (Proizvod) o;
        return id == proizvod.id &&
                Objects.equals(naziv, proizvod.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naziv);
    }

    @ManyToOne
    @JoinColumn(name = "ID_ZAKONSKA_PODLAGA", referencedColumnName = "ID")
    public ZakonskaPodlaga getZakonskaPodlaga() {
        return zakonskaPodlaga;
    }

    public void setZakonskaPodlaga(ZakonskaPodlaga zakonskaPodlaga) {
        this.zakonskaPodlaga = zakonskaPodlaga;
    }

    // NO JsonIgnore here, because we need to have it in proizvod View
    @ManyToOne
    @JoinColumn(name = "ID_ZASCITEN_PROIZVOD", referencedColumnName = "ID", nullable = false)
    public ZascitenProizvod getZascitenProizvod() {
        return zascitenProizvod;
    }

    public void setZascitenProizvod(ZascitenProizvod zascitenproizvodByIdZascitenProizvod) {
        this.zascitenProizvod = zascitenproizvodByIdZascitenProizvod;
    }

    // NO JsonIgnore here, because we need to have it in proizvod View
    @JsonIgnore
    @OneToMany(mappedBy = "proizvod")
    public Collection<ZascitniznakProizvod> getZascitniznakProizvod() {
        return zascitniznakProizvod;
    }

    public void setZascitniznakProizvod(Collection<ZascitniznakProizvod> value) {
        this.zascitniznakProizvod = value;
    }

    @Basic
    @Column(name = "NEAKTIVEN")
    public boolean isNeaktiven() {
        return neaktiven;
    }

    public void setNeaktiven(boolean neaktiven) {
        this.neaktiven = neaktiven;
    }

    @Basic
    @Column(name = "ENOTA")
    public String getEnota() {
        return enota;
    }

    public void setEnota(String enota) {
        this.enota = enota;
    }

    private Long spremenil;

    @Basic
    @Column(name = "SPREMENIL")
    public Long getSpremenil() {
        return spremenil;
    }

    public void setSpremenil(Long spremenil) {
        this.spremenil = spremenil;
    }
}
