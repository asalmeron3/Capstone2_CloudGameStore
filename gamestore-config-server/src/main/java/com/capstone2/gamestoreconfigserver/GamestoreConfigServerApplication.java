package com.capstone2.gamestoreconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class GamestoreConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamestoreConfigServerApplication.class, args);
	}

}
