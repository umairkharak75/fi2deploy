package zaslontelecom.esk.backend.api.Service;
import com.sun.xml.fastinfoset.util.CharArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Utils.HandledException;


import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.Arrays;

@Service
public class AuthenticationService {
    @Autowired
    EntityManager em;

    public UserWithPermissions login(String username, char[] password)  {
        try {
            StoredProcedureQuery spLogin = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.login");
            // register parameters
            spLogin.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            spLogin.setParameter(1, username);
            spLogin.registerStoredProcedureParameter(2, char[].class, ParameterMode.IN);
            spLogin.setParameter(2, password);
            spLogin.registerStoredProcedureParameter(3, Long.class, ParameterMode.OUT);
            spLogin.execute();

            Long id = (Long) spLogin.getOutputParameterValue(3);
            Arrays.fill(password, '*');

            if (id == null || id == 0) {
                throw new BadCredentialsException("INVALID");
            }

            return this.loadByUsername(username);

        } catch(Exception ex){
            throw new BadCredentialsException("INVALID");
        }
    }

    private UserWithPermissions loadByUsername(String username) {
        try {
            StoredProcedureQuery spProfile = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.get_profile_data");
            // register parameters
            spProfile.registerStoredProcedureParameter("USERNAME", String.class, ParameterMode.IN);
            spProfile.setParameter("USERNAME", username);
            spProfile.registerStoredProcedureParameter("ORG_SIF", String.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("ORG_IME", String.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("ORG_NASLOV", String.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("USER_ID", Long.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("DELAVEC_IME", String.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("MAIL", String.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("TEL", String.class, ParameterMode.OUT);
            spProfile.registerStoredProcedureParameter("PRAVICE", String.class, ParameterMode.OUT);

            spProfile.execute();

            UserWithPermissions user = new UserWithPermissions();
            user.setOrgSif((String) spProfile.getOutputParameterValue("ORG_SIF"));
            user.setOrgIme((String) spProfile.getOutputParameterValue("ORG_IME"));
            user.setOrgNaslov((String) spProfile.getOutputParameterValue("ORG_NASLOV"));
            user.setId((Long) spProfile.getOutputParameterValue("USER_ID"));
            user.setDelavecIme((String) spProfile.getOutputParameterValue("DELAVEC_IME"));
            user.setMail((String) spProfile.getOutputParameterValue("MAIL"));
            user.setTel((String) spProfile.getOutputParameterValue("TEL"));
            user.setPravice((String) spProfile.getOutputParameterValue("PRAVICE"));
            user.setUserName(username);
            return user;
        }catch (Exception ex){
            throw new BadCredentialsException("INVALID");
        }
    }
}