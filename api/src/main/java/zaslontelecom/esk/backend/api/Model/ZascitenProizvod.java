package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "ZASCITENPROIZVOD")
public class ZascitenProizvod {
    private long id;
    private String naziv;
    private Shema shema;
    private Long spremenil;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZASCITENPROIZVOD_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.ZASCITENPROIZVOD_seq", allocationSize = 1, name = "ZASCITENPROIZVOD_seq")
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
        ZascitenProizvod that = (ZascitenProizvod) o;
        return id == that.id &&
                Objects.equals(naziv, that.naziv);
    }
/*
    @OneToMany(mappedBy = "zascitenproizvodByIdZascitenProizvod")
    public Collection<Certifikat> getCertifikatsById() {
        return certifikatsById;
    }

    public void setCertifikatsById(Collection<Certifikat> certifikatsById) {
        this.certifikatsById = certifikatsById;
    }

    @OneToMany(mappedBy = "zascitenProizvod")
    public Collection<Proizvod> getProizvodsById() {
        return proizvodsById;
    }

    public void setProizvodsById(Collection<Proizvod> proizvodsById) {
        this.proizvodsById = proizvodsById;
    }

    @OneToMany(mappedBy = "zascitenproizvodByIdZascitenProizvod")
    public Collection<ZascitniZnak> getZascitniznaksById() {
        return zascitniznaksById;
    }

    public void setZascitniznaksById(Collection<ZascitniZnak> zascitniznaksById) {
        this.zascitniznaksById = zascitniznaksById;
    }

    @Basic
    @Column(name = "ID_SHEMA", nullable = false, precision = 0)
    public long getIdShema() {
        return idShema;
    }

    public void setIdShema(long idShema) {
        this.idShema = idShema;
    }*/

    @Override
    public int hashCode() {
        return Objects.hash(id, naziv);
    }

    @ManyToOne
    @JoinColumn(name = "ID_SHEMA", referencedColumnName = "ID", nullable = false)
    public Shema getShema() {
        return shema;
    }

    public void setShema(Shema shemaByIdShema) {
        this.shema = shemaByIdShema;
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
