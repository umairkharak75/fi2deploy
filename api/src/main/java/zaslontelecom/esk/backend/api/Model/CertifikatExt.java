package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "CERTIFIKAT_EXT")
public class CertifikatExt {
    private long id;
    private String idImis;
    private long idCertifikat;
    private Long spremenil;
    private Time datSpremembe;
    private String pdf;

    @Basic
    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFIKAT_EXT_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.CERTIFIKAT_EXT_seq", allocationSize = 1, name = "CERTIFIKAT_EXT_seq")
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ID_IMIS")
    public String getIdImis() {
        return idImis;
    }

    public void setIdImis(String idImis) {
        this.idImis = idImis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertifikatExt that = (CertifikatExt) o;
        return id == that.id &&
                Objects.equals(idImis, that.idImis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idImis);
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
    @Column(name = "SPREMENIL")
    public Long getSpremenil() {
        return spremenil;
    }

    public void setSpremenil(Long spremenil) {
        this.spremenil = spremenil;
    }

    @Basic
    @Column(name = "DAT_SPREMEMBE")
    public Time getDatSpremembe() {
        return datSpremembe;
    }

    public void setDatSpremembe(Time datSpremembe) {
        this.datSpremembe = datSpremembe;
    }

    @Basic
    @Column(name = "PDF")
    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
