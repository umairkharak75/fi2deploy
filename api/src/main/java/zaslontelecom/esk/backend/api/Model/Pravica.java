package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Pravica {
    private long id;
    private String naziv;
    private String tip;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAZIV", nullable = true, length = 2000)
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Basic
    @Column(name = "TIP", nullable = true, length = 2000)
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pravica pravica = (Pravica) o;
        return id == pravica.id &&
                Objects.equals(naziv, pravica.naziv) &&
                Objects.equals(tip, pravica.tip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naziv, tip);
    }

}
