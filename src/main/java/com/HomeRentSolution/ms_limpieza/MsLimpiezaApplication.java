package com.HomeRentSolution.ms_limpieza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsLimpiezaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLimpiezaApplication.class, args);
	}

}
