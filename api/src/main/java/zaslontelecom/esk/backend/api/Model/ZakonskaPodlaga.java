package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Time;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "ZAKONSKAPODLAGA")
public class ZakonskaPodlaga {
    private long id;
    private String stevilka;
    private String vsebina;
    private boolean neaktiven;
    private Long spremenil;
    private Time datSpremembe;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZAKONSKAPODLAGA_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.ZAKONSKAPODLAGA_seq", allocationSize = 1, name = "ZAKONSKAPODLAGA_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "STEVILKA", nullable = true, length = 2000)
    public String getStevilka() {
        return stevilka;
    }

    public void setStevilka(String stevilka) {
        this.stevilka = stevilka;
    }

    @Basic
    @Column(name = "VSEBINA", nullable = true)
    public String getVsebina() {
        return vsebina;
    }

    public void setVsebina(String vsebina) {
        this.vsebina = vsebina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZakonskaPodlaga that = (ZakonskaPodlaga) o;
        return id == that.id &&
                Objects.equals(stevilka, that.stevilka) &&
                Objects.equals(vsebina, that.vsebina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stevilka, vsebina);
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
