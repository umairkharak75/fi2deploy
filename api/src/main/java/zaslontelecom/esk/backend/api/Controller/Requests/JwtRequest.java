package zaslontelecom.esk.backend.api.Controller.Requests;

public class JwtRequest {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    String username;
    char[] password;
}
