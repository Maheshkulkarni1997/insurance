package org.safeguard.insurance.service;

import java.io.IOException;
import java.io.InputStream;

import org.safeguard.insurance.base.BaseInsurancePolicy;
import org.safeguard.insurance.config.PdfGenerationFactory;
import org.safeguard.insurance.config.PdfGenerationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	public void sendEmailWithAttachment(String to, String subject, BaseInsurancePolicy insurancePolicy)
			throws MessagingException, IOException {

		Context context = new Context();
		context.setVariable("policyNumber", insurancePolicy.getPolicyNumber());
		context.setVariable("policyHolderName", insurancePolicy.getPolicyHolderName());
		context.setVariable("coverageDetails", insurancePolicy.getCoverageDetails());
		context.setVariable("premiumAmount", insurancePolicy.getPremiumAmount());
		context.setVariable("deductible", insurancePolicy.getDeductible());
		context.setVariable("networkList", insurancePolicy.getNetworkList());
		context.setVariable("claimProcessDetails", insurancePolicy.getClaimProcessDetails());
		context.setVariable("exclusions", insurancePolicy.getExclusions());
		context.setVariable("policyType", insurancePolicy.getPolicyType());
		context.setVariable("expiryDate", insurancePolicy.getExpiryDate());
		context.setVariable("nextPremiumDate", insurancePolicy.getNextPremiumDate());

		String htmlContent = templateEngine.process("emailTemplate", context);

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom("your-email@example.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		PdfGenerationInterface strategy = PdfGenerationFactory.getStrategy(insurancePolicy);

		try (InputStream attachmentStream = strategy.generatePdf(insurancePolicy)) {
			helper.addAttachment("PolicyDetails.pdf", new ByteArrayDataSource(attachmentStream, "application/pdf"));
		}

		javaMailSender.send(message);
	}
}
