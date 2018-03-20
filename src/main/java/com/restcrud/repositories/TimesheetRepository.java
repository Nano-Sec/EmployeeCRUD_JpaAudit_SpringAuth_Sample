package com.restcrud.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.restcrud.entities.Timesheet;

@Component
public interface TimesheetRepository extends CrudRepository<Timesheet, Long> {
	List<Timesheet> findByEmployeeEmployeeNumber(long id);
	@Query("select timesheetNumber from Timesheet where date(:date) not in (select date(dateOfLog) from Timesheet) and employee.employeeNumber= :id")
	List<Timesheet> checkDate(@Param("date") Date dateOfLog,@Param("id") long id);
}
