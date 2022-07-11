package com.example.AED.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AED.entity.Employee;
public interface EmployeeRepository extends MongoRepository<Employee,Long>{

	
}
