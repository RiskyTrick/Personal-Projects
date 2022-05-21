package com.example.CMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.example.CMS.entity.Client;
import com.example.CMS.service.ClientService;

@Controller
public class ClientController {

	private ClientService clientService;

	// when ever the spring bean has only one constructor then we can avoid using autowired annotation so we have not made use of @Autowired in our project.
	public ClientController(ClientService clientService) {
		super();
		this.clientService = clientService;
	}
	
	//handler
	
	@GetMapping("/Clients")
	public String getClientsList(Model model) {
		
		model.addAttribute("Clients", clientService.getAllClients());
		
		return "Clients";
		
	}
	
	@GetMapping("/Clients/new")
	public String CreateClientForm(Model model) {
		
		Client client=new Client();
		model.addAttribute("client",client);
		return "Create_Client";
	}
	
	@PostMapping("/Clients")
	public String saveClient(@ModelAttribute("Clients") Client Client) {
	clientService.saveClient(Client);
	return "redirect:/Clients";
	}
	
	@GetMapping("/Clients/edit/{id}")
	public String editStudentForm(@PathVariable Long id,Model model) {
		model.addAttribute("client",clientService.getClientById(id));
		return "edit_student";
	}
	
	@PostMapping("/Clients/{id}")
	public String updateClient(@PathVariable Long id,
		@ModelAttribute("client") Client client, Model model) {
			//fetching client data from DB
			Client existingClient= clientService.getClientById(id);
			
			// Updating the data 
			existingClient.setId(id);
			existingClient.setFirstname(client.getFirstname());
			existingClient.setLastname(client.getLastname());
			existingClient.setEmail(client.getEmail());
			
			//saving the updated data
			clientService.updateClient(existingClient);
			
			return"redirect:/Clients";

		}
	//handler for delete req
	//@DeleteMapping("/Clients/{id}")//delete mapping not working in browser.
	@GetMapping("/Clients/{id}")
	public String deleteClient(@PathVariable Long id) {
		clientService.deleteClientById(id);
		
		return "redirect:/Clients";
	}
	
}//end bracket

