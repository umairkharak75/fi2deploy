package zaslontelecom.esk.backend.api.Controller;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zaslontelecom.esk.backend.api.Controller.Requests.JwtRequest;
import zaslontelecom.esk.backend.api.Controller.Response.JwtResponse;
import zaslontelecom.esk.backend.api.Model.MyUserPrincipal;
import zaslontelecom.esk.backend.api.Security.CustomAuthenticationProvider;
import zaslontelecom.esk.backend.api.Security.UserActivityList;
import zaslontelecom.esk.backend.api.Service.MyUserDetailsService;
import zaslontelecom.esk.backend.api.Security.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController

@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private CustomAuthenticationProvider authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    UserActivityList activityList;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        if (authenticationRequest.getUsername() == null) {
            throw new UsernameNotFoundException("Unknown username");
        }
        this.activityList.removeUserActivity(authenticationRequest.getUsername());
        this.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final MyUserPrincipal userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return getAuthenticationResponseEntity(token);
    }

    private void authenticate(String username, char[] password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        // kill password for security reasons (Memory DUMP)
        Arrays.fill(password, '*');
    }

    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return getAuthenticationResponseEntity(token);
    }

    private ResponseEntity<?> getAuthenticationResponseEntity(String token) {
        final MyUserPrincipal userDetails = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
        JwtResponse response = new JwtResponse();
        response.setToken(token);
        response.setUser(userDetails);
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }
}