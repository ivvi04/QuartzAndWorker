package ru.lakeevda.quartztest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
public class QuartzTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzTestApplication.class, args);
    }

}
