package com.example.AED.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.AED.entity.Employee;
import com.example.AED.service.EmployeeService;


@RestController
public class EmployeeController {
	
	
	@Autowired
	private EmployeeService service;

	
	@PostMapping("/addEmp")
	public String saveEmployee(@RequestBody Employee employee) {
		service.saveEmployee(employee);
		return "Saved Employee Data with id:"+employee.getId();
	}
	
	@GetMapping("/findEmp/{id}")
	public Employee getEmployee(@PathVariable Long id){
		 return service.getEmployee(id);
	}
	
}
