package zaslontelecom.esk.backend.api.Security;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import zaslontelecom.esk.backend.api.Controller.Response.CustomErrorResponse;
import zaslontelecom.esk.backend.api.Model.MyUserPrincipal;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Service.MyUserDetailsService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    UserActivityList users;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // JWT Token is in the form "Bearer token". Remove Bearer word and
            // get only the Token
            String jwtToken = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                if (!users.isActive(username)){
                    throw new Exception("User was not active in avaliable period time.");
                }

                UserWithPermissions user = this.users.getUserActivity(username);
                this.users.setUserActivity(user);

                UserDetails userDetails = new MyUserPrincipal(user);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Cannot set the Security Context");
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            String isRefreshToken = request.getHeader("isRefreshToken");
            String requestURL = request.getRequestURL().toString();
            // allow for Refresh Token creation if following conditions are true.
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, request);
                chain.doFilter(request, response);
            } else
            {
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(request, response, new AuthenticationException("EXPIRED") {});
            }

        } catch (BadCredentialsException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new AuthenticationException("INVALID") {});
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new AuthenticationException("INVALID") {});
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }
}