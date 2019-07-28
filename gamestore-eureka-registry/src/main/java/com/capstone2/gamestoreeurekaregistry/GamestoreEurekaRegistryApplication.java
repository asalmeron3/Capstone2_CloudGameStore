package com.capstone2.gamestoreeurekaregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class GamestoreEurekaRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamestoreEurekaRegistryApplication.class, args);
	}

}
