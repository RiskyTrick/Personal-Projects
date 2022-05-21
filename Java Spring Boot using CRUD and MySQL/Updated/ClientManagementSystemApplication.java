package com.example.CMS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientManagementSystemApplication implements CommandLineRunner {
	

	public static void main(String[] args) {
		SpringApplication.run(ClientManagementSystemApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
//		Client clientObj = new Client("Bhavesh","Naidu","bhavesh.ricky@gmail.com","",(long)1);
//		clientRepository.save(clientObj);
		
//		new File(ClientController.uploadDirectory).mkdir();
//		SpringApplication.run(ClientController.class, args);
	}

}
