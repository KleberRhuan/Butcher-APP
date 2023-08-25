package io.github.kleberrhuan.butcherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ButcherappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ButcherappApplication.class, args);
    }

}
