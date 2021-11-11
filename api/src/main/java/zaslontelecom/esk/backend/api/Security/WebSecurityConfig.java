package zaslontelecom.esk.backend.api.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import zaslontelecom.esk.backend.api.Utils.Settings;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@ComponentScan("zaslontelecom.esk.backend.api.Security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    Settings settings;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String[] resources = new String[]{
                "/dashboard", "/angular/**", "/home", "/assets/**", "/assets/img/**",
                "/css/**","/icons/**","/images/**","/js/**","/layer/**"
        };

        // dont authenticate this particular request
        httpSecurity
                //.cors().and().csrf().disable()
                .authorizeRequests()
                //.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers(resources).permitAll()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/certifikat/list").permitAll()
                //.antMatchers("/certifikat/invalidate").permitAll()
                .antMatchers("/files/**").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                //.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //httpSecurity.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        //httpSecurity.headers().contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**", "/assets/**", "/assets/img/**", "/resources/**"); // THIS solved MIME type error
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        //config.setAllowedOrigins(Collections.singletonList(this.settings.clientAddress()));
        config.setAllowedOrigins(Arrays.asList("*"));
        //config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept","Authorization"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean registration = new FilterRegistrationBean(new CorsFilter(source));
        return registration;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(settings.fileSize());
        return multipartResolver;
    }
}