package com.example.AED.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.AED.entity.Employee;
import com.example.AED.repository.EmployeeRepository;


@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository repository;

	
	@PostMapping("/addEmp")
	public String saveEmployee(@RequestBody Employee employee) {
		repository.save(employee);
		return "Saved Employee Data with id:"+employee.getId();
	}
	
	@GetMapping("/fetchEmp")
	public List<Employee> getEmployee(){
		return repository.findAll();
	}
	
	@GetMapping("/findEmp/{id}")
	public Optional<Employee> getEmployee(@PathVariable Long id){
		return repository.findById(id);
	}
	
	@DeleteMapping("/deleteEmp/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		
		return "Employee deleted with id:"+id;
	}
	
}
