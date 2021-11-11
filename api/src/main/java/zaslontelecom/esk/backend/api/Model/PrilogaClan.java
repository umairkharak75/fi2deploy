package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "PRILOGA_CLAN")
public class PrilogaClan {
    private long id;
    private Long idPriloga;
    private Long idSubjekt;
    private Priloga priloga;
    private Subjekt clan;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRILOGA_CLAN_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.PRILOGA_CLAN_seq", allocationSize = 1, name = "PRILOGA_CLAN_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ID_PRILOGA", nullable = true, precision = 0)
    public Long getIdPriloga() {
        return idPriloga;
    }

    public void setIdPriloga(Long idPriloga) {
        this.idPriloga = idPriloga;
    }

    @Basic
    @Column(name = "ID_SUBJEKT", nullable = true, precision = 0)
    public Long getIdSubjekt() {
        return idSubjekt;
    }

    public void setIdSubjekt(Long idSubjekt) {
        this.idSubjekt = idSubjekt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrilogaClan that = (PrilogaClan) o;
        return id == that.id &&
                Objects.equals(idPriloga, that.idPriloga) &&
                Objects.equals(idSubjekt, that.idSubjekt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPriloga, idSubjekt);
    }

    // DO NOT REMOVE
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_PRILOGA", referencedColumnName = "ID", insertable=false, updatable=false)
    public Priloga getPriloga() {
        return priloga;
    }

    public void setPriloga(Priloga value) {
        this.priloga = value;
    }

    @ManyToOne
    @JoinColumn(name = "ID_SUBJEKT", referencedColumnName = "ID", insertable=false, updatable=false)
    public Subjekt getClan() {
        return clan;
    }

    public void setClan(Subjekt value) {
        this.clan = value;
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
