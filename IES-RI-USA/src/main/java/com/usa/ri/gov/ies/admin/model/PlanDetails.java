package com.usa.ri.gov.ies.admin.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PlanDetails {
	
	private int planId;
	
	private String planName;
	
	private String planDescription;
	
	//@DateTimeFormat(pattern="MM-dd-yyyy")
	private String planStartDate;
	
	//@DateTimeFormat(pattern="MM-dd-yyyy")
	private String planEndDate;
	
	private String activeSw;
	
	private String createdBy;
	
	private String updatedBy;
	
	private Date createDate;
	
	private Date updateDate;

	
}
