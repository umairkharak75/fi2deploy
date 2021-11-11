package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Kolicine {
    private long id;
    private Long vrednost;
    private String enota;
    private Long leto;
    private long idProizvod;
    private long idCertifikat;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KOLICINE_seq")
    @SequenceGenerator(sequenceName = "KOLICINE_seq", allocationSize = 1, name = "KOLICINE_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "VREDNOST", nullable = true, precision = 0)
    public Long getVrednost() {
        return vrednost;
    }

    public void setVrednost(Long vrednost) {
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
    @Column(name = "LETO", nullable = true, precision = 0)
    public Long getLeto() {
        return leto;
    }

    public void setLeto(Long leto) {
        this.leto = leto;
    }

    @Basic
    @Column(name = "ID_PROIZVOD", nullable = false, precision = 0)
    public long getIdProizvod() {
        return idProizvod;
    }

    public void setIdProizvod(long idProizvod) {
        this.idProizvod = idProizvod;
    }

    @Basic
    @Column(name = "ID_CERTIFIKAT", nullable = false, precision = 0)
    public long getIdCertifikat() {
        return idCertifikat;
    }

    public void setIdCertifikat(long idCertifikat) {
        this.idCertifikat = idCertifikat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kolicine kolicine = (Kolicine) o;
        return id == kolicine.id &&
                idProizvod == kolicine.idProizvod &&
                idCertifikat == kolicine.idCertifikat &&
                Objects.equals(vrednost, kolicine.vrednost) &&
                Objects.equals(enota, kolicine.enota) &&
                Objects.equals(leto, kolicine.leto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vrednost, enota, leto, idProizvod, idCertifikat);
    }
}
