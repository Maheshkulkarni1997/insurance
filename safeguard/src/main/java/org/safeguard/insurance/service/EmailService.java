package org.safeguard.insurance.service;

import org.safeguard.insurance.config.PdfGenerationInterface;
import org.safeguard.insurance.config.PdfGenerationFactory;
import org.safeguard.insurance.base.BaseInsurancePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service for sending emails with attachments.
  */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Send an email with an attachment.
     * 
     * @param to the recipient email address
     * @param subject the email subject
     * @param insurancePolicy the insurance policy details to include in the email
     * @throws MessagingException if an error occurs while sending the email
     * @throws IOException if an error occurs while generating the PDF
     */
    public void sendEmailWithAttachment(String to, String subject, BaseInsurancePolicy insurancePolicy) 
            throws MessagingException, IOException {

        // Set up the email context with policy details
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

        // Generate the HTML content using Thymeleaf template
        String htmlContent = templateEngine.process("emailTemplate", context);

        // Create the email message
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("your-email@example.com");  // Replace with your email
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);  // Set to true for HTML content

        // Get the appropriate PDF generation strategy
        PdfGenerationInterface strategy = PdfGenerationFactory.getStrategy(insurancePolicy);

        // Create and attach the PDF
        try (InputStream attachmentStream = strategy.generatePdf(insurancePolicy)) {
            helper.addAttachment("PolicyDetails.pdf", new ByteArrayDataSource(attachmentStream, "application/pdf"));
        }

        // Send the email
        javaMailSender.send(message);
    }
}
