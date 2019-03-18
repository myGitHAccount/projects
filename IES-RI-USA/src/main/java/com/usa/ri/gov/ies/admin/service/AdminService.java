package com.usa.ri.gov.ies.admin.service;

import java.util.List;

import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.PlanDetails;

public interface AdminService {

	public boolean registerAppAccount(AppAccount accModel);

	public String checkDuplicateEmail(String email);

	public List<AppAccount> findAllAccounts();

	public boolean updateActiveSw(int accId, String activeSw);

	public boolean createPlan(PlanDetails planDetails);

	public String checkDuplicatePlan(String plan);
	
	public List<PlanDetails> findAllPlans();
	
	public boolean updatePlanActiveSw(int planId, String activeSw);

}
