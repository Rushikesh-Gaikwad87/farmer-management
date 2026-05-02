package com.farm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FarmerManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmerManagementApplication.class, args);
    }

}