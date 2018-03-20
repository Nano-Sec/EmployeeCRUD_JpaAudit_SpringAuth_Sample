package com.restcrud.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.restcrud.entities.ContractEmployee;
import com.restcrud.entities.Employee;
import com.restcrud.entities.RegularEmployee;
import com.restcrud.entities.Timesheet;
import com.restcrud.entities.UserRole;
import com.restcrud.repositories.ContractEmployeeRepository;
import com.restcrud.repositories.EmployeeRepository;
import com.restcrud.repositories.RegularEmployeeRepository;
import com.restcrud.repositories.TimesheetRepository;

@Service
public class EmployeeService {
	
	public static final Logger LOGGER= LoggerFactory.getLogger(EmployeeService.class);
	ObjectMapper mapper= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Autowired
	private EmployeeRepository empRepo;
	@Autowired
	private RegularEmployeeRepository regularRepo;
	@Autowired
	private ContractEmployeeRepository contractRepo;
	@Autowired
	private TimesheetRepository trepo;
	
	public List<Object> getAllEmployees(){
		List<Object> emps= new ArrayList<Object>();
		empRepo.findAll()
		.forEach(emps::add);
		return emps;
	}
	public void addEmployee(Employee emp) throws JsonParseException, JsonMappingException, IOException {
		setRoles(emp);
		empRepo.save(emp);
		LOGGER.info("Employee "+ emp.getEmployeeName()+ " added");
	}
	public void setRoles(Employee emp) {
		Builder<UserRole> builder = new ImmutableList.Builder<UserRole>();
		UserRole userRole=new UserRole();
		userRole.setUser(emp);
		for(UserRole role: emp.getUserRoles()) {
			userRole.setRoleType(role.getRoleType());
			builder = builder.add(userRole);
		}
		emp.setUserRoles(builder.build());
	}
	public Object getEmployee(long id) {
		return empRepo.findOne(id);
	}
	public void updateEmployee(Employee emp) {
		setRoles(emp);
		empRepo.save(emp);
		LOGGER.info("Employee "+emp.getEmployeeName()+" updated");
	}
	public void addTimesheet(Employee fetched, Timesheet singleSheet) {
		LOGGER.info("Adding timesheet for " + fetched.getEmployeeName());
        if(trepo.checkDate(singleSheet.getDateOfLog(), fetched.getEmployeeNumber()).isEmpty() &&
        		!trepo.findByEmployeeEmployeeNumber(fetched.getEmployeeNumber()).isEmpty()) {
        	LOGGER.error("Only one timesheet per day can be added for an employee");
        }
        else {
        	singleSheet.setEmployee(fetched);
        	trepo.save(singleSheet);
        }
	}
	public List<Timesheet> getTimesheet(long id){
		return trepo.findByEmployeeEmployeeNumber(id);
	}
	public double getEmpMonthlyCost(Object emp, int month){
		double cost=0;
		if(emp instanceof ContractEmployee) {
			cost= contractRepo.getConEmpMonthCost(((ContractEmployee)emp).getEmployeeNumber());
		}
		else if(emp instanceof RegularEmployee) {
	        cost= regularRepo.getRegEmpMonthCost(month, ((RegularEmployee) emp).getEmployeeNumber());
		}
		else {
			LOGGER.error("invalid employee type");
			return 0;
		}
		return cost;
	}
	public double getTotalMonthlyCost(int month) {
		double cost=0;
		LOGGER.info("Fetching cost for the month " + month);
        cost= regularRepo.getTotRegMonthCost(month);
        cost+= contractRepo.getTotConMonthCost();
        return cost;
	}
	public List<Object[]> getTotalDeptMonthlyCost(int month){
		List<Object[]> regularList= regularRepo.getDeptMonthCost(month);
		List<Object[]> contractList= contractRepo.getDeptMonthCost();
		List<Object[]> totalList= getDeptList(regularList, contractList);
		return totalList;
	}
	public List<Object[]> getDeptList(List<Object[]> regularList,List<Object[]> contractList){
		for(int i=0;i< regularList.size();i++) {
			for(int j=0; j< contractList.size();j++) {
				if(regularList.get(i)[0].equals(contractList.get(j)[0])) {
					double temp= ((double)regularList.get(i)[1])+((double)contractList.get(j)[1]);
					regularList.set(i, new Object[] {regularList.get(i)[0],temp});
					contractList.remove(j);
				}
			}
		}
		regularList.addAll(contractList);
		return regularList;
	}
}
