package zaslontelecom.esk.backend.api.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class Settings {

    @Autowired
    private Environment env;

    public String getPdfTemplatePath(){
        return env.getProperty("esk.template.path");
    }

    public String getDownloadPath(){
        return env.getProperty("esk.download.path");
    }

    public String getWorkingPath(){
        return env.getProperty("esk.working.path");
    }

    public String getImisHost() {
        return env.getProperty("imis.host");
    }

    public String getImisUsername() {
        return env.getProperty("imis.username");
    }

    public char[] getImisPassword() {return env.getProperty("imis.password", char[].class);}

    public String getImisStage() {return env.getProperty("imis.stage");}

    public String clientAddress() {
        String address = env.getProperty("client.address");
        return address;
    }

    public long fileSize() {return Long.valueOf(env.getProperty("file.size"));}

    public boolean getImisEnabled() {
        return Boolean.valueOf(env.getProperty("imis.enable"));
    }

    public double getBottomMarginCert(String org, double defVal) {
        String value = env.getProperty("template.certifikat.bottom.margin." + org);
        return value == null ? defVal : Double.valueOf(value);
    }

    public double getBottomMarginAtt(String org, double defVal) {
        String value = env.getProperty("template.priloga.bottom.margin." + org);
        return value == null ? defVal : Double.valueOf(value);
    }

    public String getAntiSamyPath() {
        String res = env.getProperty("antisamy.filePath");
        return res;
    }

    public long jwtTokenValidityInMs() {
        String value = env.getProperty("jwt.expirationDateInMs");
        return value == null ? (8*60*60*1000) : Long.valueOf(value);
    }

    public long jwtRefreshExpirationDateInMs() {
        String value = env.getProperty("jwt.refreshExpirationDateInMs");
        return value == null ? (7200000) : Long.valueOf(value);
    }

    public long getMaxInactivityTimeInMs() {
        String value = env.getProperty("user.maxInactivityTimeInMs");
        return value == null ? (7200000) : Long.valueOf(value);
    }

    public String getFindDuplicates() {
        String res = env.getProperty("certificates.find-duplicates");
        if (res == null){
            res = "WITH_VALIDATION_DATE";
        }
        return res;
    }
}
