package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "ZASCITNIZNAK_PROIZVOD")
public class ZascitniznakProizvod {
    private long id;
    private long idZascitniznak;
    private long idProizvod;
    private Zascitniznak zascitniznak;
    private Proizvod proizvod;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZASCITNIZNAK_PROIZVOD_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.ZASCITNIZNAK_PROIZVOD_seq", allocationSize = 1, name = "ZASCITNIZNAK_PROIZVOD_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ID_ZASCITNIZNAK")
    public long getIdZascitniznak() {
        return idZascitniznak;
    }

    public void setIdZascitniznak(long value) {
        this.idZascitniznak = value;
    }

    @Basic
    @Column(name = "ID_PROIZVOD")
    public long getIdProizvod() {
        return idProizvod;
    }

    public void setIdProizvod(long value) {
        this.idProizvod = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZascitniznakProizvod that = (ZascitniznakProizvod) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_ZASCITNIZNAK", referencedColumnName = "ID", insertable = false, updatable = false)
    public Zascitniznak getZascitniznak() {
        return zascitniznak;
    }

    public void setZascitniznak(Zascitniznak value) {
        this.zascitniznak = value;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_PROIZVOD", referencedColumnName = "ID", insertable = false, updatable = false)
    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod value) {
        this.proizvod = value;
    }
}
