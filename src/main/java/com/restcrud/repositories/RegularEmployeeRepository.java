package com.restcrud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restcrud.entities.RegularEmployee;

public interface RegularEmployeeRepository extends CrudRepository<RegularEmployee, Long> {
	@Query("select sum(hoursWorked)*(select billingRatePerHour from RegularEmployee where employeeNumber= :id) from Timesheet where month(dateOfLog)= :month"
			+ " and employee.employeeNumber= :id")
	double getRegEmpMonthCost(@Param("month") int month, @Param("id") long id);
	@Query("select sum(emp.billingRatePerHour*sheet.hoursWorked) from RegularEmployee emp inner "
			+ "join emp.list sheet where month(sheet.dateOfLog)= :month and emp.employeeNumber=sheet.employee.employeeNumber")
	double getTotRegMonthCost(@Param("month") int month);
	@Query("select sheet.employee.department,sum(emp.billingRatePerHour*sheet.hoursWorked) from RegularEmployee emp inner "
			+ "join emp.list sheet where month(sheet.dateOfLog)= :month and emp.employeeNumber=sheet.employee.employeeNumber group by sheet.employee.department")
	List<Object[]> getDeptMonthCost(@Param("month") int month);
}
