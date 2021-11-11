package zaslontelecom.esk.backend.api.Utils;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import oracle.jdbc.OracleDriver;


@WebListener // register it as you wishimport java.sql.Driver;
public class OjdbcDriverRegistrationListener implements ServletContextListener {


    private Driver driver = null;

    /**
     * Registers the Oracle JDBC driver
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.driver = new OracleDriver(); // load and instantiate the class
        boolean skipRegistration = false;
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver instanceof OracleDriver) {
                OracleDriver alreadyRegistered = (OracleDriver) driver;
                if (alreadyRegistered.getClass() == this.driver.getClass()) {
                    // same class in the VM already registered itself
                    skipRegistration = true;
                    this.driver = alreadyRegistered;
                    break;
                }
            }
        }

        try {
            if (!skipRegistration) {
                DriverManager.registerDriver(driver);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deregisters JDBC driver
     *
     * Prevents Tomcat 7 from complaining about memory leaks.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (this.driver != null) {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
            }
            this.driver = null;
        }

    }

}