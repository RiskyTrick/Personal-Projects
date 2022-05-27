package com.example.CMS.service;
import java.util.*;

import com.example.CMS.entity.Client;

public interface ClientService {

	List<Client> getAllClients();
	
	Client saveClient(Client client);
	
	Client getClientById(Long id);
	
	Client updateClient(Client client);
	
	void deleteClientById(Long id);
}
