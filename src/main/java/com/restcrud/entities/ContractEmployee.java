package com.restcrud.entities;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.restcrud.utils.Views;

@Entity
public class ContractEmployee extends Employee {
	private static final long serialVersionUID = 1L;
	@JsonView(Views.Employee.class)
	double salaryPerAnnum;

	public double getBillingRatePerAnnum() {
		return salaryPerAnnum;
	}

	public void setBillingRatePerAnnum(double billingRatePerAnnum) {
		this.salaryPerAnnum = billingRatePerAnnum;
	}
}
