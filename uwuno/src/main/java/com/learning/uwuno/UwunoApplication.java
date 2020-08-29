package com.learning.uwuno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UwunoApplication {

    public static void main(String[] args) {
        // Need to pass application class to this + the args from the main to get this to work
        SpringApplication.run(UwunoApplication.class, args);
    }

}
