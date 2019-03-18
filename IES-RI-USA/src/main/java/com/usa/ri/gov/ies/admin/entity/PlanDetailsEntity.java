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

@Data
@Entity
@Table(name="PLAN_DTLS_TABLE")
public class PlanDetailsEntity {

	@Id
	@GeneratedValue
	@Column(name="PLAN_ID")
	private int planId;
	
	@Column(name="PLAN_NAME",unique=true)
	private String planName;
	
	@Column(name="PLAN_DESC")
	private String planDescription;
	
	@Column(name="START_DT")
	private String planStartDate;
	
	@Column(name="END_DT")
	private String planEndDate;
	
	@Column(name="ACTIVE_SW")
	private String activeSw;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@CreationTimestamp
	private Date createDate;
	
	@UpdateTimestamp
	private Date updateDate;

}
