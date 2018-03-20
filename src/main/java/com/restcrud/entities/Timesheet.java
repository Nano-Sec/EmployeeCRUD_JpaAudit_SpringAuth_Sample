package com.restcrud.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(value = { "employee" })
public class Timesheet {
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="employeeNumber")
	private Employee employee;
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long timesheetNumber;
	Date dateOfLog;
	String workDone;
	int hoursWorked;

	public Date getDateOfLog() {
		return dateOfLog;
	}
	public void setDateOfLog(Date dateOfLog) {
		this.dateOfLog = dateOfLog;
	}
	public String getWorkDone() {
		return workDone;
	}
	public void setWorkDone(String workDone) {
		this.workDone = workDone;
	}
	public int getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
