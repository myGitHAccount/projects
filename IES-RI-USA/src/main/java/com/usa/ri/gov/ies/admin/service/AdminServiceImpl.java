package com.usa.ri.gov.ies.admin.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usa.ri.gov.ies.admin.constants.Constants;
import com.usa.ri.gov.ies.admin.entity.AppAccountEntity;
import com.usa.ri.gov.ies.admin.entity.PlanDetailsEntity;
import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.PlanDetails;
import com.usa.ri.gov.ies.admin.properties.AppProperties;
import com.usa.ri.gov.ies.admin.repository.AppAccountRepository;
import com.usa.ri.gov.ies.admin.repository.PlanDetailsRepository;
import com.usa.ri.gov.ies.admin.util.MailUtils;
import com.usa.ri.gov.ies.admin.util.PasswordUtils;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

	private Logger logger = (Logger) LoggerFactory.getLogger(AdminServiceImpl.class);
	@Autowired(required = true)
	private AppAccountRepository appAccRepository;

	@Autowired(required = true)
	private MailUtils mailUtils;

	@Autowired(required = true)
	private AppProperties appProperties;

	@Autowired(required = true)
	private PlanDetailsRepository planRepository;

	/**
	 * This method is used store case worker details in database
	 *
	 * @Param cwModel
	 * @return boolean
	 */
	@Override
	public boolean registerAppAccount(AppAccount accModel) {
		logger.debug(" Account Registration started");
		String encryptedPwd = null;
		// create AppAccount Entity object
		AppAccountEntity entity = new AppAccountEntity();

		// convert model object to entity
		BeanUtils.copyProperties(accModel, entity);

		// convert the password to encrypted password
		encryptedPwd = PasswordUtils.encrypt(entity.getPassword());
		entity.setPassword(encryptedPwd);
		try {

			// set activeSw to Y
			entity.setActiveSw(Constants.ACTIVE_SW);

			// call repository method
			AppAccountEntity appAccEntity = appAccRepository.save(entity);

			String fileName = appProperties.getProperties().get(Constants.REG_EMAIL_FILE_NAME);
			String mailSub = appProperties.getProperties().get(Constants.ACCOUNT_REG_SUBJECT);
			String mailBody = createMailBody(accModel, fileName);
			// sending confirmation mail
			mailUtils.sendEmail(accModel.getEmail(), mailSub, mailBody);
			logger.debug(" Account Registration ended");

			if (appAccEntity.getAccId() > 0)
				return true;
		} catch (Exception e) {
			logger.error("User Registation failed" + e.getMessage());
		}
		logger.info("Acccount Registartion Successfull");
		return false;
	}

	/**
	 * This method is used to create the body of email
	 * 
	 * @param model
	 * @return String
	 * @throws Exception
	 */

	private String createMailBody(AppAccount model, String fileName) throws Exception {
		logger.debug("AdmnServiceImpl:: createMailBody started");
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		if (br != null) {
			line = br.readLine();
			while (line != null) {
				if (!line.equals("<br/>") && line.length() != 0) {
					if (line.contains("USER_NAME")) {
						line = line.replace("USER_NAME", model.getFirstName() + " " + model.getLastName());
					}
					if (line.contains("APP_URL")) {
						line = line.replace("APP_URL", appProperties.getProperties().get(Constants.PROJECT_URL));
					}
					if (line.contains("APP_USER_EMAIL")) {
						line = line.replace("APP_USER_EMAIL", model.getEmail());
					}
					if (line.contains("APP_USER_PWD")) {
						line = line.replace("APP_USER_PWD", model.getPassword());
					}
				}
				sb.append(line);
				line = br.readLine();
			}
		}
		br.close();
		logger.debug("AdminServiceImpl:: createMailBody ended");
		logger.debug("Email body created SuccessFully");
		return sb.toString();
	}// createMailBody

	/**
	 * This method is used form checking email id Uniqueness
	 * 
	 * @param email
	 * @return String
	 */
	@Override
	public String checkDuplicateEmail(String email) {
		logger.debug("CW Service:: checkDuplicateEmail method started");

		// call Repository method
		AppAccountEntity cwEntity = appAccRepository.findByEmail(email);

		logger.debug("CW Service:: checkDuplicateEmail method started");

		return cwEntity == null ? "Unique" : "Duplicate";

	}

	/**
	 * This method is used to get all application Accounts
	 * 
	 * @return List
	 */

	@Override
	public List<AppAccount> findAllAccounts() {
		logger.debug("AdminServiceImpl:: findAllAccount method started");
		List<AppAccount> models = new ArrayList<AppAccount>();

		try {
			List<AppAccountEntity> entities = appAccRepository.findAll();

			// checking for emptiness
			if (!entities.isEmpty()) {
				// convert case worker entities into models
				for (AppAccountEntity entity : entities) {
					AppAccount model = new AppAccount();
					BeanUtils.copyProperties(entity, model);
					models.add(model);
				}
			}
		} catch (Exception e) {
			logger.error("Finding of Accounts method executio failed " + e.getMessage());
		}
		logger.debug("AdminServiceImpl:: findAllAccount method started");
		logger.info("findAllAccounts completed ");

		return models;
	}

	/**
	 * This method used to perform soft delete of a account
	 * 
	 * @param id
	 * @return String
	 */
	@Override
	public boolean updateActiveSw(int accId, String activeSw) {
		logger.debug(" AdminServiceImpl ::updateActiveSw  started ");
		try {
			// load the record from database table
			AppAccountEntity entity = appAccRepository.findById(accId).get();
			// set active status to Y|N
			entity.setActiveSw(activeSw);

			// update The record in database table
			appAccRepository.save(entity);

			AppAccount model = new AppAccount();
			BeanUtils.copyProperties(entity, model);
			// de_crypt the password
			String deCryptedPwd = PasswordUtils.decrypt(model.getPassword());
			// convert entity to model
			model.setPassword(deCryptedPwd);

			// send Account Activation Email
			if (activeSw.equals("Y")) {
				try {
					// get Account Activation Email Subject
					String emailSub = appProperties.getProperties().get(Constants.ACC_ACTIVATION_EMAIL_SUB);
					// get Activation file name
					String activationFileName = appProperties.getProperties()
							.get(Constants.ACCOUNT_ACTIVATE_EMAIL_FILE_NAME);

					// get Activate Account Email Body
					String emailBody = createMailBody(model, activationFileName);

					// send Activation mail
					mailUtils.sendEmail(entity.getEmail(), emailSub, emailBody);
				} catch (Exception e) {
					logger.error("Account Activation Email sending failed" + e.getMessage());
				}
			}
			// send Account Activation Email
			else {
				try {
					// get Account DeActivation Email Subject
					String emailSub = appProperties.getProperties().get(Constants.ACC_DE_ACTIVATION_EMAIL_SUB);
					// get Activation file name
					String deActivationFileName = appProperties.getProperties()
							.get(Constants.ACCOUNT_DE_ACTIVATE_EMAIL_FILE_NAME);

					// get Activate Account Email Body
					String emailBody = createMailBody(model, deActivationFileName);

					// send Activation mail
					mailUtils.sendEmail(entity.getEmail(), emailSub, emailBody);
				} catch (Exception e) {
					logger.error("Account Deactivation Email Sending failed");
				}
			}

			logger.debug(" AdminServiceImpl ::updateActiveSw ended ");
			logger.info("updateActiveSw Successfully completed");
			return true;
		} catch (Exception e) {
			logger.error("Record Updation failed" + e.getMessage());
		}
		return false;
	}

	/**
	 * This method is used to register plan
	 * 
	 * @param planDetails
	 * @return boolean
	 */
	@Override
	public boolean createPlan(PlanDetails planModel) {
		logger.debug("AdminServiceImpl createPlan method started");
		try {
			// convert model to entity
			PlanDetailsEntity planEntity = new PlanDetailsEntity();

			BeanUtils.copyProperties(planModel, planEntity);
				// set Active Switch to "Y"
				planEntity.setActiveSw(Constants.ACTIVE_SW);
				// set Created And Updated By
				planEntity.setCreatedBy("Admin");
				planEntity.setUpdatedBy("Admin");
				// call repository method
				PlanDetailsEntity repoPlanEntity = planRepository.save(planEntity);
				
				logger.debug("AdminServiceImpl registerPlan method started");
				logger.info("Plan created Successfully");
				if (repoPlanEntity.getPlanId() > 0)
				return true;
			
		}catch(Exception e){
			logger.error("Plan Creation Failed");
		}
		return false;
	}

	/**
	 * This method is used form checking plan name Uniqueness
	 * 
	 * @param plan
	 * @return String
	 */
	@Override
	public String checkDuplicatePlan(String plan) {
		logger.debug("CW Service:: checkDuplicatePlan method started");

		// call Repository method
		PlanDetailsEntity planEntity = planRepository.findByPlanName(plan);

		logger.debug("CW Service:: checkDuplicatePlan method started");

		return planEntity == null ? "Unique" : "Duplicate";

	}
	/**
	 * This method is used to get all application Accounts
	 * 
	 * @return List
	 */

	@Override
	public List<PlanDetails> findAllPlans() {
		logger.debug("AdminServiceImpl:: findAllPlans method started");
		List<PlanDetails> models = new ArrayList<PlanDetails>();

		try {
			List<PlanDetailsEntity> entities = planRepository.findAll();

			// checking for emptiness
			if (!entities.isEmpty()) {
				// convert PlanDetail entities into models
				for (PlanDetailsEntity entity : entities) {
					PlanDetails model = new PlanDetails();
					BeanUtils.copyProperties(entity, model);
					models.add(model);
				}
			}
		} catch (Exception e) {
			logger.error("Finding of Accounts method executio failed " + e.getMessage());
		}
		logger.debug("AdminServiceImpl:: findAllPlans method started");
		logger.info("findAllPlans completed ");

		return models;
	}
	
	/**
	 * This method used to perform soft delete of a Plan
	 * 
	 * @param id
	 * @return String
	 */
	@Override
	public boolean updatePlanActiveSw(int planId, String activeSw) {
		logger.debug(" AdminServiceImpl ::updatePlanActiveSw  started ");
		try {
			// load the record from database table
			PlanDetailsEntity entity = planRepository.findById(planId).get();
			// set active status to Y|N
			entity.setActiveSw(activeSw);

			// update The record in database table
			PlanDetailsEntity planEntity=planRepository.save(entity);
			
			logger.debug(" AdminServiceImpl ::updatePlanActiveSw ended ");
			logger.info("updatePlanActiveSw Successfully completed");
			// send Account Activation Email
			if (planEntity!=null) {
				return true;
			}

		} catch (Exception e) {
			logger.error("Record Updation failed" + e.getMessage());
		}
		return false;
	}
}
