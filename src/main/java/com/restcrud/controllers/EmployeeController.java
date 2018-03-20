package com.restcrud.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restcrud.entities.ContractEmployee;
import com.restcrud.entities.Employee;
import com.restcrud.entities.RegularEmployee;
import com.restcrud.entities.Timesheet;
import com.restcrud.services.AuthService;
import com.restcrud.services.EmployeeService;
import com.restcrud.utils.Views;

@RestController
public class EmployeeController {
	public static final Logger LOGGER= LoggerFactory.getLogger(EmployeeController.class);
	ObjectMapper mapper= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Autowired
	EmployeeService service;
	@Autowired
	private AuthService authService;
	@RequestMapping("/user")
	public Object getLoggedInUser() {
		return authService.getLoggedInUser();
	}
	
	//List all employees
	@GetMapping(value = "/admin/all") @JsonView(Views.Employee.class)
	public ResponseEntity<List<Object>> getAllEmployees(){
		List <Object> list= service.getAllEmployees();
		if(list.isEmpty()) {
			return new ResponseEntity<List<Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Object>>(list, HttpStatus.OK);
	}
	
	//View employee for admin
	@JsonView(Views.Employee.class)
	@GetMapping(value = "/admin/view/{id}")
    public ResponseEntity<Object> getEmployee(@PathVariable("id") long id) {
        Object emp= service.getEmployee(id);
		if(emp == null) {
        	LOGGER.error("Employee with id " + id + " not found");
        	return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(emp, HttpStatus.OK);
    }
	
	//View logged in employee
	@JsonView(Views.Employee.class)
	@GetMapping(value = "/employee/view")
    public ResponseEntity<Object> getEmployee() {
		return new ResponseEntity<Object>(authService.getLoggedInUser(), HttpStatus.OK);
    }
	
	//Insert employee
	@PostMapping(value = "/admin/insert/{type}")
    public ResponseEntity<Void> addEmployee(@RequestBody String emp,@PathVariable("type") String type) throws JsonParseException, JsonMappingException, IOException {
		Employee employee= mapper.readValue(emp, Employee.class);
		LOGGER.info("Creating User " + employee.getEmployeeName());
        if(service.getEmployee(employee.getEmployeeNumber())!= null){
            LOGGER.error("A User with id " + employee.getEmployeeNumber() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        service.addEmployee(transform(type,emp));
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	//Add timesheet
	@PostMapping(value = "/employee/timesheet/insert")
    public ResponseEntity<Void> addTimesheet(@RequestBody Timesheet singleSheet) {
        Employee fetched= (Employee)authService.getLoggedInUser();
		if(fetched== null) {
			LOGGER.error("No match found for timesheet id");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
			
		service.addTimesheet(fetched, singleSheet);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	//View timesheet for admin
	@GetMapping(value = "/admin/timesheet/view/{id}")
    public ResponseEntity<List<Timesheet>> getTimesheet(@PathVariable("id") long id) {
        LOGGER.info("Fetching Timesheet of employee with id " + id);
        Employee emp = (Employee) service.getEmployee(id);
        if (emp == null) {
            LOGGER.error("Employee with id " + id + " not found");
            return new ResponseEntity<List<Timesheet>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Timesheet>>(service.getTimesheet(id), HttpStatus.OK);
    }
	
	//View timesheet
	@GetMapping(value = "/employee/timesheet/view")
    public void getTimesheet() {
		long id = ((Employee)authService.getLoggedInUser()).getEmployeeNumber();
		getTimesheet(id);
    }
	
	//View monthly employee cost
	//Enter month in digits, ex: 2 for feb
	@GetMapping(value = "/admin/cost/{month}/{id}")
    public ResponseEntity<Double> getEmployeeCost(@PathVariable("month") int month,@PathVariable("id") long id) {
        LOGGER.info("Fetching monthly cost of employee with id " + id);
        Object emp = service.getEmployee(id);
        if (emp == null) {
            LOGGER.error("Employee with id " + id + " not found");
            return new ResponseEntity<Double>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Double>(service.getEmpMonthlyCost(emp, month), HttpStatus.OK);
    }
	
	//View total monthly cost
	//Enter month in digits, ex: 2 for feb
	@GetMapping(value = "/admin/cost/{month}")
    public ResponseEntity<Double> getMonthlyCost(@PathVariable("month") int month) {
        return new ResponseEntity<Double>(service.getTotalMonthlyCost(month), HttpStatus.OK);
    }
	
	//View total monthly cost by department
	//Enter month in digits, ex: 2 for feb
	@GetMapping(value = "/admin/cost/dept/{month}")
    public ResponseEntity<List<Object[]>> getMonthlyDeptCost(@PathVariable("month") int month) {
        return new ResponseEntity<List<Object[]>>(service.getTotalDeptMonthlyCost(month), HttpStatus.OK);
    }
		
	//Update employee
	@RequestMapping(value = "/admin/update/{type}", method=RequestMethod.PUT)
    public ResponseEntity<Employee> updateEmployee(@RequestBody String emp,@PathVariable("type") String type) throws JsonParseException, JsonMappingException, IOException {
		Employee employee= mapper.readValue(emp, Employee.class);
		long id= employee.getEmployeeNumber();
        Employee currentEmployee = (Employee) service.getEmployee(id);
        if (currentEmployee==null) {
            LOGGER.error("User with id " + id + " not found");
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
        service.updateEmployee(transform(type,emp));
        return new ResponseEntity<Employee>(currentEmployee, HttpStatus.OK);
    }
	public Employee transform(String type, String emp) throws JsonParseException, JsonMappingException, IOException {
		Employee employee = null;
		switch(type) {
        case "regular":
        	employee= mapper.readValue(emp, RegularEmployee.class);
        	break;
        case "contract":
        	employee= mapper.readValue(emp, ContractEmployee.class);
        	break;
    	default:
    		LOGGER.error("invalid employee type");
    		break;
        }
		return employee;
	}
}
