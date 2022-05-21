package com.example.CMS.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.CMS.entity.Client;
import com.example.CMS.service.ClientService;
import com.example.CMS.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService{

	private ClientRepository clientRepository;
	
	
	// when ever the spring bean has only one constructor then we can avoid using autowired annotation so we have not made use of @Autowired in our project. same as in controller ;_; .
	public ClientServiceImpl(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}



	@Override
	public List<Client> getAllClients(){
		
		return clientRepository.findAll();
	}
	
	@Override
	public Client saveClient(Client client) {
		return clientRepository.save(client);
	}



	@Override
	public Client getClientById(Long id) {
		
		return clientRepository.findById(id).get();
	}



	@Override
	public Client updateClient(Client client) {
		
		return clientRepository.save(client);
	}



	@Override
	public void deleteClientById(Long id) {
		clientRepository.deleteById(id);
		
	}
}
