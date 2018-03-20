package com.restcrud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.restcrud.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	public Employee findByUsername(String username);
}
