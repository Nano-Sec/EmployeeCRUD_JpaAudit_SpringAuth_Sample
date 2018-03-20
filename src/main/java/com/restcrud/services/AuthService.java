package com.restcrud.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	public Object getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
		       return null;
		}
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
