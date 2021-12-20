package com.volcano.campsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class CampgroundApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        System.out.println("Environment port = " + System.getProperty("server.port"));
        SpringApplication.run(CampgroundApplication.class, args);
    }

}
