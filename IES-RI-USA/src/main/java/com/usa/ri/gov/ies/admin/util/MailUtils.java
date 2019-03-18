package com.usa.ri.gov.ies.admin.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailUtils {

	@Autowired
	private JavaMailSender sender;

	public  boolean sendEmail(String to,String subject,String body) throws Exception {
		boolean flag = false;
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setTo(to);
			helper.setText(body,true);
			helper.setSubject(subject);
			sender.send(message);
			flag = true;
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
}
