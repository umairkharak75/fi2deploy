package zaslontelecom.esk.backend.api.Controller.Requests;

public class ZascitenProizvodByIdShemaQuery extends PagedQuery {
    private long idShema;

    public long getIdShema() {
        return idShema;
    }

    public void setIdShema(long idShema) {
        this.idShema = idShema;
    }
}
