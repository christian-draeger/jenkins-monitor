package de.monitor.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by christian.draeger on 15.01.16.
 */
@SpringBootApplication
@ComponentScan("de.monitor")
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}