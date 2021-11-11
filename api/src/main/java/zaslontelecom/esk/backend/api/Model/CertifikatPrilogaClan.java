package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "CERTIFIKAT_PRILOGA_CLAN")
public class CertifikatPrilogaClan {
    private long id;
    private Long idCertifikat;
    private Long idPriloga;
    private Certifikat certifikat;
    private Priloga priloga;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFIKAT_PRILOGA_CLAN_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.CERTIFIKAT_PRILOGA_CLAN_seq", allocationSize = 1, name = "CERTIFIKAT_PRILOGA_CLAN_seq")
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
    @Column(name = "ID_PRILOGA", nullable = true, precision = 0)
    public Long getIdPriloga() {
        return idPriloga;
    }

    public void setIdPriloga(Long idPriloga) {
        this.idPriloga = idPriloga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertifikatPrilogaClan that = (CertifikatPrilogaClan) o;
        return id == that.id &&
                Objects.equals(idCertifikat, that.idCertifikat) &&
                Objects.equals(idPriloga, that.idPriloga);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCertifikat, idPriloga);
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_CERTIFIKAT", referencedColumnName = "ID", insertable=false, updatable=false)
    public Certifikat getCertifikat() {
        return certifikat;
    }

    public void setCertifikat(Certifikat certifikat) {
        this.certifikat = certifikat;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PRILOGA", referencedColumnName = "ID", insertable=false, updatable=false)
    public Priloga getPriloga() {
        return priloga;
    }

    public void setPriloga(Priloga priloga) {
        this.priloga = priloga;
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
