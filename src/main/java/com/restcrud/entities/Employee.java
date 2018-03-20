package com.restcrud.entities;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonView;
import com.restcrud.entities.UserRole;
import com.restcrud.utils.Enums.BloodGroup;
import com.restcrud.utils.Enums.Department;
import com.restcrud.utils.Views;


@Entity @EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED) 
public class Employee implements UserDetails{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @JsonView(Views.Employee.class)
	long employeeNumber;
	@JsonView(Views.Employee.class)
	String employeeName;
	@JsonView(Views.Employee.class)
	Department department;
	@Column(name = "username", unique = true) @JsonView(Views.Employee.class)
	private String username;
	@Column(name = "password")
	private String password;
	@JsonView(Views.Employee.class)
	Date dateOfJoining;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<UserRole> userRoles;
	@OneToMany(mappedBy="employee")
	private List<Timesheet> list;
	@JsonView(Views.Employee.class)
	private BloodGroup bloodGroup;
	@CreatedBy @ManyToOne @JoinColumn(updatable=false) @JsonView(Views.Employee.class)
    protected Employee createdBy;
    @CreatedDate @Column(nullable = false, updatable = false) @JsonView(Views.Employee.class)
    protected Date creationDate;
    @LastModifiedBy @ManyToOne @JsonView(Views.Employee.class)
    protected Employee lastModifiedBy;
    @LastModifiedDate @Column(nullable = false) @JsonView(Views.Employee.class)
    protected Date lastModifiedDate;
	public Employee() {}
	public Employee(String username, String password, String name) {
		this.username = username;
		this.employeeName = name;
		this.password = password;
		this.lastModifiedBy= createdBy;
		this.lastModifiedDate= creationDate;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getUserRoles();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	public long getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(long employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Date getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRole) {
		this.userRoles = userRole;
	}
	public String getCreatedBy() {
		return createdBy.getEmployeeName();
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy.getEmployeeName();
	}
	public void setLastModifiedBy(Employee lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
