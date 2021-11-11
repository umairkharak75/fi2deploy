package zaslontelecom.esk.backend.api.Controller.Response;

import zaslontelecom.esk.backend.api.Model.Subjekt;

import java.util.Collection;

public class RefreshResponse {

    public Subjekt getImetnik() {
        return imetnik;
    }

    public void setImetnik(Subjekt imetnik) {
        this.imetnik = imetnik;
    }

    Subjekt imetnik;

    private Collection<Subjekt> clani;

    public Collection<Subjekt> getClani() {
        return clani;
    }

    public void setClani(Collection<Subjekt> clani) {
        this.clani = clani;
    }
}
