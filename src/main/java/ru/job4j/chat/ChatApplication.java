package ru.job4j.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ChatApplication.class.getSimpleName());

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        LOG.info("Go to page http://localhost:8080/");
    }

}
