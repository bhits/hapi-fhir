package ca.uhn.fhir.jpa.subscription.email;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class EmailSender implements IEmailSender {

	private static final Logger ourLog = LoggerFactory.getLogger(EmailSender.class);
	private String mySmtpServerHost;
	private int mySmtpServerPort = 25;
	private JavaMailSenderImpl mySender;

	@PostConstruct
	public void start() {
		Validate.notBlank(mySmtpServerHost, "No SMTP host defined");

		mySender = new JavaMailSenderImpl();
		mySender.setHost(mySmtpServerHost);
		mySender.setPort(mySmtpServerPort);
		mySender.setDefaultEncoding(Constants.CHARSET_UTF8.name());
	}

	@Override
	public void send(EmailDetails theDetails) {
		ourLog.info("Sending email to recipients: {}", theDetails.getTo());
		StopWatch sw = new StopWatch();

		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.TEXT);

		SpringStandardDialect dialect = new SpringStandardDialect();
		dialect.setEnableSpringELCompiler(true);

		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setDialect(dialect);
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver);

		Context context = new Context();

		String body = engine.process(theDetails.getBodyTemplate(), context);
		String subject = engine.process(theDetails.getSubjectTemplate(), context);

		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(trim(theDetails.getFrom()));
		email.setTo(toTrimmedStringArray(theDetails.getTo()));
		email.setSubject(subject);
		email.setText(body);
		email.setSentDate(new Date());

		mySender.send(email);

		ourLog.info("Done sending email (took {}ms)", sw.getMillis());
	}

	/**
	 * Set the SMTP server host to use for outbound mail
	 */
	public void setSmtpServerHost(String theSmtpServerHost) {
		mySmtpServerHost = theSmtpServerHost;
	}

	/**
	 * Set the SMTP server port to use for outbound mail
	 */
	public void setSmtpServerPort(int theSmtpServerPort) {
		mySmtpServerPort = theSmtpServerPort;
	}

	private static String[] toTrimmedStringArray(List<String> theTo) {
		List<String> to = new ArrayList<>();
		for (String next : theTo) {
			if (isNotBlank(next)) {
				to.add(next);
			}
		}
		return to.toArray(new String[to.size()]);
	}
}
