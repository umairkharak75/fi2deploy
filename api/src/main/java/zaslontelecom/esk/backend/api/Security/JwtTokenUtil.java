package zaslontelecom.esk.backend.api.Security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import zaslontelecom.esk.backend.api.Model.MyUserPrincipal;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Utils.Settings;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    @Autowired
    Settings settings;

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(MyUserPrincipal userDetails) {

        UserWithPermissions u = userDetails.getEskUser();
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions",u.getPravice());
        claims.put("id", u.getId());
        claims.put("name", u.getDelavecIme());
        claims.put("mail", u.getMail());
        claims.put("orgName", u.getOrgIme());
        claims.put("orgSif", u.getOrgSif());
        claims.put("orgNaslov", u.getOrgNaslov());
        claims.put("phone", u.getTel());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // while creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Long validationTimeInMs = settings.jwtTokenValidityInMs();
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validationTimeInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + settings.jwtRefreshExpirationDateInMs()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID", ex);
        }
    }
}
