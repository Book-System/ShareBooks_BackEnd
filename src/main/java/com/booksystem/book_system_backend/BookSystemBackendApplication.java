package com.booksystem.book_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "com.booksystem.entity" })
@EnableJpaRepositories(basePackages = { "com.booksystem.repository" })
@ComponentScan(basePackages = { "com.booksystem.controller", "com.booksystem.restcontroller", "com.booksystem.security",
		"com.booksystem.jwt", "com.booksystem.service", "com.booksystem.scheduler" })
public class BookSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSystemBackendApplication.class, args);
		System.out.println("START SERVER");
	}
}