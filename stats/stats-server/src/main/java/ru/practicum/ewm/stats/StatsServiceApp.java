package ru.practicum.ewm.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.ewm")
public class StatsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(StatsServiceApp.class, args);
    }

}
