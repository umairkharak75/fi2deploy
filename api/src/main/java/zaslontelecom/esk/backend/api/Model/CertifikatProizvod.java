package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "CERTIFIKAT_PROIZVOD")
public class CertifikatProizvod {
    private long id;
    private Long idCertifikat;
    private Long idProizvod;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFIKAT_PROIZVOD_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.CERTIFIKAT_PROIZVOD_seq", allocationSize = 1, name = "CERTIFIKAT_PROIZVOD_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ID_CERTIFIKAT", nullable = true, precision = 0)
    public Long getIdCertifikat() {
        return idCertifikat;
    }

    public void setIdCertifikat(Long idCertifikat) {
        this.idCertifikat = idCertifikat;
    }

    @Basic
    @Column(name = "ID_PROIZVOD", nullable = true, precision = 0)
    public Long getIdProizvod() {
        return idProizvod;
    }

    public void setIdProizvod(Long idProizvod) {
        this.idProizvod = idProizvod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertifikatProizvod that = (CertifikatProizvod) o;
        return id == that.id &&
                Objects.equals(idCertifikat, that.idCertifikat) &&
                Objects.equals(idProizvod, that.idProizvod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCertifikat, idProizvod);
    }

    /*@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CERTIFIKAT", referencedColumnName = "ID", insertable=false, updatable=false)
    public Certifikat getCertifikat() {
        return certifikat;
    }

    public void setCertifikat(Certifikat certifikat) {
        this.certifikat = certifikat;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_PROIZVOD", referencedColumnName = "ID", insertable=false, updatable=false)
    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }*/
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
