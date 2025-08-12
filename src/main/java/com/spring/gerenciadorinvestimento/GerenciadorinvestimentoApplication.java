package com.spring.gerenciadorinvestimento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GerenciadorinvestimentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciadorinvestimentoApplication.class, args);
	}
}
