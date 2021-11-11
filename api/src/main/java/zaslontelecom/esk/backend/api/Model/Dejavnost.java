package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "DEJAVNOST")
public class Dejavnost {
    private long id;
    private String naziv;
    @JsonIgnore
    private Collection<Certifikat> certifikatsById;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEJAVNOST_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.DEJAVNOST_seq", allocationSize = 1, name = "DEJAVNOST_seq")
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
        Dejavnost dejavnost = (Dejavnost) o;
        return id == dejavnost.id &&
                Objects.equals(naziv, dejavnost.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naziv);
    }

    @OneToMany(mappedBy = "dejavnost")
    public Collection<Certifikat> getCertifikatsById() {
        return certifikatsById;
    }

    public void setCertifikatsById(Collection<Certifikat> certifikatsById) {
        this.certifikatsById = certifikatsById;
    }

    private boolean neaktiven;

    @Basic
    @Column(name = "NEAKTIVEN")
    public boolean isNeaktiven() {
        return neaktiven;
    }

    public void setNeaktiven(boolean neaktiven) {
        this.neaktiven = neaktiven;
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
