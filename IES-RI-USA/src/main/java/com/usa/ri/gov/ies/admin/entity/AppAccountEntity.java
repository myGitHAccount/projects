package com.usa.ri.gov.ies.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "APP_ACCOUNTS_ENTITY_TABLE")
public class AppAccountEntity {

	@Id
	@GeneratedValue
	@Column(name = "ACC_ID")
	private int accId;
	
	@Column(name = "FNAME")
	private String firstName;
	
	@Column(name = "LNAME")
	private String lastName;
	
	@Column(name = "GENDER")
	private String gender;

	@Column(name = "PHNO")
	private String phno;
	
	@Column(name = "EMAIL", unique = true)
	private String email;
	
	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "DOB")
	private String dob;
	
	@Column(name = "SSN")
	private long ssn;

	@Column(name = "ACTIVE_SW")
	private String activeSw;
	
	@CreationTimestamp
	private Date createDate;
	
	@UpdateTimestamp
	private Date updateDate;

	@Column(name = "ROLE")
	private String role;
}
