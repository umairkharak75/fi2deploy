package zaslontelecom.esk.backend.api.Controller.Response;

public class DashboardResponse {
    public int getVeljavniTotal() {
        return veljavniTotal;
    }

    public void setVeljavniTotal(int veljavniTotal) {
        this.veljavniTotal = veljavniTotal;
    }

    public int getvIzteku() {
        return vIzteku;
    }

    public void setvIzteku(int vIzteku) {
        this.vIzteku = vIzteku;
    }

    public int getVnosTotal() {
        return vnosTotal;
    }

    public void setVnosTotal(int vnosTotal) {
        this.vnosTotal = vnosTotal;
    }

    int veljavniTotal = 0;
    int vIzteku = 0;
    int vnosTotal = 0;
}
