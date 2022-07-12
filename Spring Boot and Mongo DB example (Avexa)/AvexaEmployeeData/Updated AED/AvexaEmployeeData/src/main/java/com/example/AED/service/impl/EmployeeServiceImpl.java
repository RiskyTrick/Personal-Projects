package com.example.AED.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AED.entity.Employee;
import com.example.AED.repository.EmployeeRepository;
import com.example.AED.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository repository;
	
	@Override
	public void saveEmployee(Employee Employee) {
		// TODO Auto-generated method stub
		repository.save(Employee);
		
	}

	@Override
	public Employee getEmployee(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
		
	}

}
