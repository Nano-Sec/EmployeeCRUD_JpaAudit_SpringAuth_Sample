package com.restcrud.entities;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.restcrud.utils.Views;

@Entity
public class RegularEmployee extends Employee {
	private static final long serialVersionUID = 1L;
	@JsonView(Views.Employee.class)
	Double billingRatePerHour;

	public Double getBillingRatePerHour() {
		return billingRatePerHour;
	}

	public void setBillingRatePerHour(Double billingRatePerHour) {
		this.billingRatePerHour = billingRatePerHour;
	}
}
