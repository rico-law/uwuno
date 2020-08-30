package com.learning.uwuno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UwunoApplication {

    public static void main(String[] args) {
        // Need to pass application class to this + the args from the main to get this to work
        // Starts tomcat server and scans the rest of the path for sprint annotations
        SpringApplication.run(UwunoApplication.class, args);
    }

}
