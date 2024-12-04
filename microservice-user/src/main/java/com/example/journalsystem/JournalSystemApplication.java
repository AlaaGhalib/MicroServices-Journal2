package com.example.journalsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {"com.example.journalsystem.bo.model"})
@SpringBootApplication(scanBasePackages = "com.example.journalsystem")
public class JournalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalSystemApplication.class, args);
    }

}
