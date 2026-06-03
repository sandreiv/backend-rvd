package co.edu.unipamplona.ciadti.rvd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RvdApplication {

    public static void main(String[] args) {
        SpringApplication.run(RvdApplication.class, args);
    }

}
