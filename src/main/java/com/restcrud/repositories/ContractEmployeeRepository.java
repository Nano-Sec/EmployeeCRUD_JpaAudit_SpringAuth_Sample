package com.restcrud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restcrud.entities.ContractEmployee;

public interface ContractEmployeeRepository extends CrudRepository<ContractEmployee, Long> {
	@Query("select salaryPerAnnum/12 from ContractEmployee where employeeNumber= :id")
	double getConEmpMonthCost(@Param("id") long id);
	@Query("select sum(salaryPerAnnum/12) from ContractEmployee")
	double getTotConMonthCost();
	@Query("select department,sum(salaryPerAnnum/12) from ContractEmployee group by department")
	List<Object[]> getDeptMonthCost();
}
