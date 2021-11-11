package zaslontelecom.esk.backend.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "CERTIFIKAT")
public class Certifikat {
    private long id;
    private String tip;
    private String stevilka;
    private Date datKontrole;
    private Date datIzdaje;
    private Date datVelj;
    private String status;
    private String telSt;
    private String email;
    private String opomba;
    private String organizacija;
    private Date datVnosa;
    private String kontrolor;
    private ZascitenProizvod zascitenProizvod;
    private Subjekt imetnik;
    private EskUsers uporabnik;
    private Dejavnost dejavnost;
    private Collection<CertifikatPrilogaClan> certifikatPrilogaClan;
    private Collection<CertifikatPrilogaProizvod> certifikatPrilogaProizvod;
    private Collection<CertifikatProizvod> certifikatProizvod;
    private Collection<Dejavnost> dejavnosti;
    private Long idParent;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CERTIFIKAT_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.CERTIFIKAT_seq", allocationSize = 1, name = "CERTIFIKAT_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TIP", nullable = true, length = 2000)
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
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
    @Column(name = "DAT_KONTROLE", nullable = true)
    public Date getDatKontrole() {
        return datKontrole;
    }

    public void setDatKontrole(Date datKontrole) {
        this.datKontrole = datKontrole;
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
    @Column(name = "TEL_ST", nullable = true, length = 2000)
    public String getTelSt() {
        return telSt;
    }

    public void setTelSt(String telSt) {
        this.telSt = telSt;
    }

    @Basic
    @Column(name = "EMAIL", nullable = true, length = 2000)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "OPOMBA", nullable = true)
    public String getOpomba() {
        return opomba;
    }

    public void setOpomba(String opomba) {
        this.opomba = opomba;
    }

    @Basic
    @Column(name = "ORGANIZACIJA", nullable = true)
    public String getOrganizacija() {
        return organizacija;
    }

    public void setOrganizacija(String organizacija) {
        this.organizacija = organizacija;
    }

    @Basic
    @Column(name = "DAT_VNOSA", nullable = true)
    public Date getDatVnosa() {
        return datVnosa;
    }

    public void setDatVnosa(Date datVnosa) {
        this.datVnosa = datVnosa;
    }

    @Basic
    @Column(name = "KONTROLOR", nullable = true, length = 2000)
    public String getKontrolor() {
        return kontrolor;
    }

    public void setKontrolor(String kontrolor) {
        this.kontrolor = kontrolor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certifikat that = (Certifikat) o;
        return id == that.id &&
                Objects.equals(tip, that.tip) &&
                Objects.equals(stevilka, that.stevilka) &&
                Objects.equals(datKontrole, that.datKontrole) &&
                Objects.equals(datIzdaje, that.datIzdaje) &&
                Objects.equals(datVelj, that.datVelj) &&
                Objects.equals(status, that.status) &&
                Objects.equals(telSt, that.telSt) &&
                Objects.equals(email, that.email) &&
                Objects.equals(opomba, that.opomba) &&
                Objects.equals(datVnosa, that.datVnosa) &&
                Objects.equals(kontrolor, that.kontrolor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tip, stevilka, datKontrole, datIzdaje, datVelj, status, telSt, email, opomba, datVnosa, kontrolor);
    }


    @ManyToOne
    @JoinColumn(name = "ID_ZASCITEN_PROIZVOD", referencedColumnName = "ID", nullable = false)
    public ZascitenProizvod getZascitenProizvod() {
        return zascitenProizvod;
    }

    public void setZascitenProizvod(ZascitenProizvod value) {
        this.zascitenProizvod = value;
    }

    @ManyToOne
    @JoinColumn(name = "ID_IMETNIK", referencedColumnName = "ID", nullable = false)
    public Subjekt getImetnik() {
        return imetnik;
    }

    public void setImetnik(Subjekt value) {
        this.imetnik = value;
    }

    @ManyToOne
    @JoinColumn(name = "ID_UPORABNIK", referencedColumnName = "USER_ID", nullable = false)
    public EskUsers getUporabnik() {
        return uporabnik;
    }

    public void setUporabnik(EskUsers value) {
        this.uporabnik = value;
    }

    @ManyToOne
    @JoinColumn(name = "ID_DEJAVNOST", referencedColumnName = "ID", nullable = false)
    public Dejavnost getDejavnost() {
        return dejavnost;
    }

    public void setDejavnost(Dejavnost value) {
        this.dejavnost = value;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "priloga")
    public Collection<CertifikatPrilogaClan> getCertifikatPrilogaClan() {
        return certifikatPrilogaClan;
    }
    @JsonProperty
    public void setCertifikatPrilogaClan(Collection<CertifikatPrilogaClan> value) {
        this.certifikatPrilogaClan = value;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "priloga")
    public Collection<CertifikatPrilogaProizvod> getCertifikatPrilogaProizvod() {
        return certifikatPrilogaProizvod;
    }
    @JsonProperty
    public void setCertifikatPrilogaProizvod(Collection<CertifikatPrilogaProizvod> value) {
        this.certifikatPrilogaProizvod = value;
    }

    @JsonIgnore
    @Transient
    public Collection<CertifikatProizvod> getCertifikatProizvod() {
        return certifikatProizvod;
    }
    @JsonProperty
    public void setCertifikatProizvod(Collection<CertifikatProizvod> value) {
        this.certifikatProizvod = value;
    }

    @Basic
    @Column(name = "ID_PARENT", nullable = true, precision = 0)
    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }

    @JsonIgnore
    @Transient
    public Collection<Dejavnost> getDejavnosti() {
        return dejavnosti;
    }
    @JsonProperty
    public void setDejavnosti(Collection<Dejavnost> value) {
        this.dejavnosti = value;
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
