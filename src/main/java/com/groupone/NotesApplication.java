package com.groupone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;

@SpringBootApplication
@EnableScheduling
public class NotesApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotesApplication.class, args);
	}

}
