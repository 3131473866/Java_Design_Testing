package com.example.diaop;

import com.example.diaop.application.WebApplication;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;

@SpringBootApplication
public class DiAopApplication implements ApplicationRunner {
	public static final String TASK_1_ENV = "task1";
	public static final String TASK_2_ENV = "task2";

	@Inject
	WebApplication webApplication;

	public static void main(String[] args) {
		SpringApplication.run(DiAopApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		webApplication.run();
	}
}
