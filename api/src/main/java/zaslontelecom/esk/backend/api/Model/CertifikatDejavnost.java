package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "CERTIFIKAT_DEJAVNOST")
public class CertifikatDejavnost {
    private long id;
    private long idCertifikat;
    private long idDejavnost;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFIKAT_DEJAVNOST_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.CERTIFIKAT_DEJAVNOST_seq", allocationSize = 1, name = "CERTIFIKAT_DEJAVNOST_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertifikatDejavnost that = (CertifikatDejavnost) o;
        return id == that.id &&
                Objects.equals(idCertifikat, that.idCertifikat) &&
                Objects.equals(idDejavnost, that.idDejavnost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCertifikat, idDejavnost);
    }

    @Basic
    @Column(name = "ID_CERTIFIKAT")
    public long getIdCertifikat() {
        return idCertifikat;
    }

    public void setIdCertifikat(long idCertifikat) {
        this.idCertifikat = idCertifikat;
    }

    @Basic
    @Column(name = "ID_DEJAVNOST")
    public long getIdDejavnost() {
        return idDejavnost;
    }

    public void setIdDejavnost(long idDejavnost) {
        this.idDejavnost = idDejavnost;
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
