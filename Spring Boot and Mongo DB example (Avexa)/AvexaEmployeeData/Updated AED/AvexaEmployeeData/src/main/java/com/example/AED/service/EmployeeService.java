package com.example.AED.service;

import com.example.AED.entity.Employee;

public interface EmployeeService {
	
	Employee saveEmployee(Employee Employee);
	Employee getEmployee(Long id);

}
