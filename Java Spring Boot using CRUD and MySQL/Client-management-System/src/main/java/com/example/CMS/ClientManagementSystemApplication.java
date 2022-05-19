package com.example.CMS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.CMS.entity.Client;
import com.example.CMS.repository.ClientRepository;

@SpringBootApplication
public class ClientManagementSystemApplication implements CommandLineRunner {
	

	public static void main(String[] args) {
		SpringApplication.run(ClientManagementSystemApplication.class, args);
	}
	
	@Autowired
    private ClientRepository clientRepository;
	@Override
	public void run(String... args) throws Exception {
		
//		Client clientObj = new Client("Bhavesh","Naidu","bhavesh.ricky@gmail.com");
//		clientRepository.save(clientObj);
		
		
	}

}
