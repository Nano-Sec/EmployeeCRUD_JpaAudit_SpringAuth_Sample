package com.restcrud.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.restcrud.entities.Employee;
import com.restcrud.repositories.EmployeeRepository;
import com.restcrud.services.AuthService;

public class AuditorAwareImpl implements AuditorAware<Employee>{
	@Autowired
	EmployeeRepository empRepo;
	@Autowired
	AuthService auth;
	@Override
	public Employee getCurrentAuditor() {
		return (Employee)auth.getLoggedInUser();
	}
}
