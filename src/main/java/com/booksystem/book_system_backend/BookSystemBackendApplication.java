package com.booksystem.book_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.booksystem.controller", "com.booksystem.restcontroller" })
@EntityScan(basePackages = { "com.example.entity" })
public class BookSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSystemBackendApplication.class, args);
		System.out.println("START SERVER");
	}
}