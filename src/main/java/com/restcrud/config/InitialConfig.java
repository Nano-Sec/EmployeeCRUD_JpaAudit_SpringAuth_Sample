package com.restcrud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.restcrud.entities.Employee;
import com.restcrud.entities.UserRole;
import com.restcrud.repositories.EmployeeRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.restcrud.utils.Enums.RoleType;

@Configuration
public class InitialConfig {
	@Autowired
	private EmployeeRepository userRepository;

	private void addUserIfAbsent(BCryptPasswordEncoder passwordEncoder, String username, String name, RoleType ...roleTypes) {
		Employee user = userRepository.findByUsername(username);
		if(user == null) {
			user = new Employee(username, passwordEncoder.encode("Pass2416"), name);
			Builder<UserRole> builder = new ImmutableList.Builder<UserRole>();
			for (RoleType roleType : roleTypes) {
				builder = builder.add(new UserRole(user, roleType));
			}
			user.setUserRoles(builder.build());
			userRepository.save(user);
		}
	}
	
	//Encoder can be passed into the configuration autowire since it's a bean
	@Autowired
	protected void setupDefaultUsers(BCryptPasswordEncoder passwordEncoder) {
		addUserIfAbsent(passwordEncoder, "admin", "Admin", RoleType.ADMIN, RoleType.EMPLOYEE);
		addUserIfAbsent(passwordEncoder, "nano", "test", RoleType.EMPLOYEE);
	}	
}
