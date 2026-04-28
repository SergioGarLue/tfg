package com.daw.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class TfgApplication {

	public static void main(String[] args) {
		// Cargar variables de entorno desde .env antes de iniciar Spring
		Dotenv.configure().systemProperties().load();
		SpringApplication.run(TfgApplication.class, args);
	}

}
