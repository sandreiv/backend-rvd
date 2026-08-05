package co.edu.unipamplona.ciadti.rvd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import co.edu.unipamplona.ciadti.rvd.config.security.SecurityAuthProperties;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties(SecurityAuthProperties.class)
public class RvdApplication {

    public static void main(String[] args) {
        SpringApplication.run(RvdApplication.class, args);
    }

}
