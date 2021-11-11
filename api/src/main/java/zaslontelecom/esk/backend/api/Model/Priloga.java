package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "PRILOGA")
public class Priloga {
    private long id;
    private String stevilka;
    private Date datIzdaje;
    private Date datVelj;
    private String status;
    private String vsebina;
    private Collection<PrilogaClan> prilogaClan;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRILOGA_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.PRILOGA_seq", allocationSize = 1, name = "PRILOGA_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "STEVILKA", nullable = true, length = 2000)
    public String getStevilka() {
        return stevilka;
    }

    public void setStevilka(String stevilka) {
        this.stevilka = stevilka;
    }

    @Basic
    @Column(name = "DAT_IZDAJE", nullable = true)
    public Date getDatIzdaje() {
        return datIzdaje;
    }

    public void setDatIzdaje(Date datIzdaje) {
        this.datIzdaje = datIzdaje;
    }

    @Basic
    @Column(name = "DAT_VELJ", nullable = true)
    public Date getDatVelj() {
        return datVelj;
    }

    public void setDatVelj(Date datVelj) {
        this.datVelj = datVelj;
    }

    @Basic
    @Column(name = "STATUS", nullable = true, length = 2000)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "VSEBINA", nullable = true)
    public String getVsebina() {
        return vsebina;
    }

    public void setVsebina(String vsebina) {
        this.vsebina = vsebina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priloga priloga = (Priloga) o;
        return id == priloga.id &&
                Objects.equals(stevilka, priloga.stevilka) &&
                Objects.equals(datIzdaje, priloga.datIzdaje) &&
                Objects.equals(datVelj, priloga.datVelj) &&
                Objects.equals(status, priloga.status) &&
                Objects.equals(vsebina, priloga.vsebina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stevilka, datIzdaje, datVelj, status, vsebina);
    }

    @OneToMany(mappedBy = "priloga")
    public Collection<PrilogaClan> getPrilogaClan() {
        return prilogaClan;
    }

    public void setPrilogaClan(Collection<PrilogaClan> prilogaClan) {
        this.prilogaClan = prilogaClan;
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
