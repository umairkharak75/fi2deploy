package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "SHEMA")
public class Shema {
    private long id;
    private String naziv;
    private Long spremenil;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHEMA_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.SHEMA_seq", allocationSize = 1, name = "SHEMA_seq")
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
        Shema shema = (Shema) o;
        return id == shema.id &&
                Objects.equals(naziv, shema.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naziv);
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
