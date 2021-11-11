package zaslontelecom.esk.backend.api.Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "ESK_DATA", name = "NASTAVITVE")
public class Nastavitve {
    private long id;
    private String verzija;
    private String appName;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NASTAVITVE_seq")
    @SequenceGenerator(sequenceName = "ESK_DATA.NASTAVITVE_seq", allocationSize = 1, name = "NASTAVITVE_seq")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "VERZIJA")
    public String getVerzija() {
        return verzija;
    }

    public void setVerzija(String verzija) {
        this.verzija = verzija;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nastavitve that = (Nastavitve) o;
        return id == that.id &&
                Objects.equals(verzija, that.verzija);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, verzija);
    }

    @Basic
    @Column(name = "APP_NAME")
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
