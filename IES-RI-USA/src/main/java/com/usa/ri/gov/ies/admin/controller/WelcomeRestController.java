package com.usa.ri.gov.ies.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usa.ri.gov.ies.admin.constants.Constants;
import com.usa.ri.gov.ies.admin.properties.AppProperties;

@RestController
public class WelcomeRestController {

	private Logger logger=LoggerFactory.getLogger(WelcomeRestController.class);

	@Autowired
	private AppProperties prop;
	
	public WelcomeRestController() {
		logger.info("WelcomeRestController::initialized");
	}
	
	@RequestMapping("/welcome")
	public String welcomeMsg() {
		logger.info("WelcomeRestController::welcome method started");
		System.out.println(prop.getProperties().get("driver.class"));
		System.out.println(prop.getProperties().get("url"));
		System.out.println(prop.getProperties().get("username"));
		System.out.println(prop.getProperties().get("password"));
		
		logger.info("WelcomeRestController::welcome method ended");

		return prop.getProperties().get(Constants.MESSAGE);
	}
}
