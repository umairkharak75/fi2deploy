package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "APP_ADMIN", name = "ESK_USERS")
public class EskUsers {
    @Id
    @Column(name = "USER_ID", nullable = false)
    private long id;
    private String orgSif;
    private String orgIme;
    private String orgNaslov;
    private String userName;
    private String delavecIme;
    private String mail;
    private String tel;

    @Basic
    @Column(name = "ORG_SIF", nullable = true, length = 30)
    public String getOrgSif() {
        return orgSif;
    }

    public void setOrgSif(String orgSif) {
        this.orgSif = orgSif;
    }

    @Basic
    @Column(name = "ORG_IME", nullable = false, length = 140)
    public String getOrgIme() {
        return orgIme;
    }

    public void setOrgIme(String orgIme) {
        this.orgIme = orgIme;
    }

    @Basic
    @Column(name = "ORG_NASLOV", nullable = true, length = 200)
    public String getOrgNaslov() {
        return orgNaslov;
    }

    public void setOrgNaslov(String orgNaslov) {
        this.orgNaslov = orgNaslov;
    }

    @Basic
    @Column(name = "USER_ID", nullable = false, precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long userId) {
        this.id = userId;
    }

    @Basic
    @Column(name = "USER_NAME", nullable = false, length = 30)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "DELAVEC_IME", nullable = false, length = 30)
    public String getDelavecIme() {
        return delavecIme;
    }

    public void setDelavecIme(String delavecIme) {
        this.delavecIme = delavecIme;
    }

    @Basic
    @Column(name = "MAIL", nullable = true, length = 100)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Basic
    @Column(name = "TEL", nullable = true, length = 30)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EskUsers eskUsers = (EskUsers) o;
        return id == eskUsers.id &&
                Objects.equals(orgSif, eskUsers.orgSif) &&
                Objects.equals(orgIme, eskUsers.orgIme) &&
                Objects.equals(orgNaslov, eskUsers.orgNaslov) &&
                Objects.equals(userName, eskUsers.userName) &&
                Objects.equals(delavecIme, eskUsers.delavecIme) &&
                Objects.equals(mail, eskUsers.mail) &&
                Objects.equals(tel, eskUsers.tel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgSif, orgIme, orgNaslov, id, userName, delavecIme, mail, tel);
    }
}
