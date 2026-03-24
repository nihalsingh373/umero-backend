package umerofirst.umero.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UmeroBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmeroBackendApplication.class, args);
	}

}
