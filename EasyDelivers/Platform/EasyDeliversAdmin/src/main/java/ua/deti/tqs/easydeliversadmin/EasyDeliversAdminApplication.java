package ua.deti.tqs.easydeliversadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
public class EasyDeliversAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyDeliversAdminApplication.class, args);
    }

}
