package zaslontelecom.esk.backend.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import zaslontelecom.esk.backend.api.Utils.OjdbcDriverRegistrationListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableScheduling
public class ApiApplication  extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new OjdbcDriverRegistrationListener());
    }
}