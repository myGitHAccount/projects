package com.usa.ri.gov.ies.admin.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AppAccount {

private int accId;
	
	private String firstName;
	
	private String lastName;
	
	private String gender;

	private String phno;
	
	private String email;
	
	private String password;

	//@DateTimeFormat(pattern="MM-dd-yyyy")
	private String dob;
	
	private long ssn;

	private String activeSw;
	
	private Date createDate;
	
	private Date updateDate;

	private String role;

}
