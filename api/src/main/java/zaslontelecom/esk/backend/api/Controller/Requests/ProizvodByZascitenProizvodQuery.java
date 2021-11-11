package zaslontelecom.esk.backend.api.Controller.Requests;

public class ProizvodByZascitenProizvodQuery extends PagedQuery {
    private int idShema;
    private int idZascitenProizvod;

    public int getIdShema() {
        return idShema;
    }

    public void setIdShema(int idShema) {
        this.idShema = idShema;
    }

    public int getIdZascitenProizvod() {
        return idZascitenProizvod;
    }

    public void setIdZascitenProizvod(int idZascitenProizvod) {
        this.idZascitenProizvod = idZascitenProizvod;
    }
}
