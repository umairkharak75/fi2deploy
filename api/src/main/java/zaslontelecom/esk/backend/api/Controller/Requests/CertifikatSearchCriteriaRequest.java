package zaslontelecom.esk.backend.api.Controller.Requests;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import zaslontelecom.esk.backend.api.Model.Dejavnost;
import zaslontelecom.esk.backend.api.Model.Proizvod;
import zaslontelecom.esk.backend.api.Model.Shema;
import zaslontelecom.esk.backend.api.Model.ZascitenProizvod;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertifikatSearchCriteriaRequest {

    @JsonProperty("proizvod")
    private List<Proizvod> proizvod = null;
    @JsonProperty("stevilka")
    private String stevilka;
    @JsonProperty("status")
    private String status;
    @JsonProperty("dejavnost")
    private Dejavnost dejavnost;
    @JsonProperty("shema")
    private Shema shema;
    @JsonProperty("zascitenProizvod")
    private ZascitenProizvod zascitenProizvod;
    @JsonProperty("datVnosaOd")
    private Date datVnosaOd;
    @JsonProperty("datVnosaDo")
    private Date datVnosaDo;
    @JsonProperty("datVeljOd")
    private Date datVeljOd;
    @JsonProperty("datVeljDo")
    private Date datVeljDo;
    @JsonProperty("datKontroleOd")
    private Date datKontroleOd;
    @JsonProperty("datKontroleDo")
    private Date datKontroleDo;
    @JsonProperty("datIzdajeOd")
    private Date datIzdajeOd;
    @JsonProperty("datIzdajeDo")
    private Date datIzdajeDo;
    @JsonProperty("kontrolor")
    private String kontrolor;
    @JsonProperty("organizacija")
    private String organizacija;
    @JsonProperty("opomba")
    private String opomba;
    @JsonProperty("subKmgmid")
    private String subKmgmid;
    @JsonProperty("subNaziv")
    private String subNaziv;
    @JsonProperty("subMaticna")
    private String subMaticna;
    @JsonProperty("subDavcna")
    private String subDavcna;
    @JsonProperty("subIdPoste")
    private String subIdPoste;
    @JsonProperty("subPosta")
    private String subPosta;
    @JsonProperty("subObcina")
    private String subObcina;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("proizvod")
    public List<Proizvod> getProizvod() {
        return proizvod;
    }

    @JsonProperty("proizvod")
    public void setProizvod(List<Proizvod> proizvod) {
        this.proizvod = proizvod;
    }

    @JsonProperty("stevilka")
    public String getStevilka() {
        return stevilka;
    }

    @JsonProperty("stevilka")
    public void setStevilka(String stevilka) {
        this.stevilka = stevilka;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("dejavnost")
    public Dejavnost getDejavnost() {
        return dejavnost;
    }

    @JsonProperty("dejavnost")
    public void setDejavnost(Dejavnost dejavnost) {
        this.dejavnost = dejavnost;
    }

    @JsonProperty("shema")
    public Shema getShema() {
        return shema;
    }

    @JsonProperty("shema")
    public void setShema(Shema value) {
        this.shema = value;
    }

    @JsonProperty("zascitenProizvod")
    public ZascitenProizvod getZascitenProizvod() {
        return zascitenProizvod;
    }

    @JsonProperty("zascitenProizvod")
    public void setZascitenProizvod(ZascitenProizvod zascitenProizvod) {
        this.zascitenProizvod = zascitenProizvod;
    }

    @JsonProperty("datVnosaOd")
    public Date getDatVnosaOd() {
        return datVnosaOd;
    }

    @JsonProperty("datVnosaOd")
    public void setDatVnosaOd(Date datVnosaOd) {
        this.datVnosaOd = datVnosaOd;
    }

    @JsonProperty("datVnosaDo")
    public Date getDatVnosaDo() {
        return datVnosaDo;
    }

    @JsonProperty("datVnosaDo")
    public void setDatVnosaDo(Date datVnosaDo) {
        this.datVnosaDo = datVnosaDo;
    }

    @JsonProperty("datVeljOd")
    public Date getDatVeljOd() {
        return datVeljOd;
    }

    @JsonProperty("datVeljOd")
    public void setDatVeljOd(Date datVeljOd) {
        this.datVeljOd = datVeljOd;
    }

    @JsonProperty("datVeljDo")
    public Date getDatVeljDo() {
        return datVeljDo;
    }

    @JsonProperty("datVeljDo")
    public void setDatVeljDo(Date datVeljDo) {
        this.datVeljDo = datVeljDo;
    }

    @JsonProperty("datKontroleOd")
    public Date getDatKontroleOd() {
        return datKontroleOd;
    }

    @JsonProperty("datKontroleOd")
    public void setDatKontroleOd(Date datKontroleOd) {
        this.datKontroleOd = datKontroleOd;
    }

    @JsonProperty("datKontroleDo")
    public Date getDatKontroleDo() {
        return datKontroleDo;
    }

    @JsonProperty("datKontroleDo")
    public void setDatKontroleDo(Date datKontroleDo) {
        this.datKontroleDo = datKontroleDo;
    }

    @JsonProperty("datIzdajeOd")
    public Date getDatIzdajeOd() {
        return datIzdajeOd;
    }

    @JsonProperty("datIzdajeOd")
    public void setDatIzdajeOd(Date datIzdajeOd) {
        this.datIzdajeOd = datIzdajeOd;
    }

    @JsonProperty("datIzdajeDo")
    public Date getDatIzdajeDo() {
        return datIzdajeDo;
    }

    @JsonProperty("datIzdajeDo")
    public void setDatIzdajeDo(Date datIzdajeDo) {
        this.datIzdajeDo = datIzdajeDo;
    }

    @JsonProperty("kontrolor")
    public String getKontrolor() {
        return kontrolor;
    }

    @JsonProperty("kontrolor")
    public void setKontrolor(String kontrolor) {
        this.kontrolor = kontrolor;
    }

    @JsonProperty("organizacija")
    public String getOrganizacija() {
        return organizacija;
    }

    @JsonProperty("organizacija")
    public void setOrganizacija(String organizacija) {
        this.organizacija = organizacija;
    }

    @JsonProperty("opomba")
    public String getOpomba() {
        return opomba;
    }

    @JsonProperty("opomba")
    public void setOpomba(String opomba) {
        this.opomba = opomba;
    }

    @JsonProperty("subKmgmid")
    public String getSubKmgmid() {
        return subKmgmid;
    }

    @JsonProperty("subKmgmid")
    public void setSubKmgmid(String subKmgmid) {
        this.subKmgmid = subKmgmid;
    }

    @JsonProperty("subNaziv")
    public String getSubNaziv() {
        return subNaziv;
    }

    @JsonProperty("subNaziv")
    public void setSubNaziv(String subNaziv) {
        this.subNaziv = subNaziv;
    }

    @JsonProperty("subMaticna")
    public String getSubMaticna() {
        return subMaticna;
    }

    @JsonProperty("subMaticna")
    public void setSubMaticna(String subMaticna) {
        this.subMaticna = subMaticna;
    }

    @JsonProperty("subDavcna")
    public String getSubDavcna() {
        return subDavcna;
    }

    @JsonProperty("subDavcna")
    public void setSubDavcna(String subDavcna) {
        this.subDavcna = subDavcna;
    }

    @JsonProperty("subIdPoste")
    public String getSubIdPoste() {
        return subIdPoste;
    }

    @JsonProperty("subIdPoste")
    public void setSubIdPoste(String subIdPoste) {
        this.subIdPoste = subIdPoste;
    }

    @JsonProperty("subPosta")
    public String getSubPosta() {
        return subPosta;
    }

    @JsonProperty("subPosta")
    public void setSubPosta(String subPosta) {
        this.subPosta = subPosta;
    }

    @JsonProperty("subObcina")
    public String getSubObcina() {
        return subObcina;
    }

    @JsonProperty("subObcina")
    public void setSubObcina(String subObcina) {
        this.subObcina = subObcina;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}