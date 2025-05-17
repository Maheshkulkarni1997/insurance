package org.safeguard.insurance.service;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.safeguard.insurance.repository.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HealthInsuranceService {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthInsuranceService.class);
    private static final DateTimeFormatter POLICY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private final HealthInsuranceRepository healthInsuranceRepository;
    private final EmailService emailService;
    
    @Autowired
    public HealthInsuranceService(HealthInsuranceRepository healthInsuranceRepository, EmailService emailService) {
        this.healthInsuranceRepository = healthInsuranceRepository;
        this.emailService = emailService;
    }
    
    @Transactional(readOnly = true)
    public List<HealthInsurance> getAllPolicies() {
        logger.debug("Fetching all health insurance policies");
        return healthInsuranceRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<HealthInsurance> getPolicyById(Long policyId) {
        logger.debug("Fetching health insurance policy with ID: {}", policyId);
        return healthInsuranceRepository.findById(policyId);
    }
    
    @Transactional
    public HealthInsurance create(HealthInsurance healthInsurance) {
        logger.info("Creating new health insurance policy");
        
        if (healthInsurance.getPolicyDate() == null) {
            healthInsurance.setPolicyDate(LocalDate.now());
            logger.debug("Policy date not provided, using current date");
        }
        
        String policyNumber = generatePolicyNumber(healthInsurance);
        healthInsurance.setPolicyNumber(policyNumber);
        logger.debug("Generated policy number: {}", policyNumber);
        
        healthInsurance.setIsActive(true);
        
        HealthInsurance savedPolicy = healthInsuranceRepository.save(healthInsurance);
        logger.info("Successfully saved health insurance policy with ID: {}", savedPolicy.getPolicyId());
        
        sendConfirmationEmail(savedPolicy);
        
        return savedPolicy;
    }
    
    @Transactional
    public void deletePolicy(Long policyId) {
        logger.info("Deleting health insurance policy with ID: {}", policyId);
        healthInsuranceRepository.deleteById(policyId);
    }
    
    @Transactional(readOnly = true)
    public List<HealthInsurance> findPoliciesByTypeAndStatus(PolicyType policyType) {
        logger.debug("Finding active policies of type: {}", policyType);
        return healthInsuranceRepository.findAllByPolicyTypeAndIsActive(policyType, true);
    }
    
    private String generatePolicyNumber(HealthInsurance healthInsurance) {
        StringBuilder policyNumberBuilder = new StringBuilder();
        policyNumberBuilder.append(healthInsurance.getPolicyType().name());
        policyNumberBuilder.append('-');
        policyNumberBuilder.append(healthInsurance.getPolicyDate().format(POLICY_DATE_FORMATTER));
        return policyNumberBuilder.toString();
    }
    
    private void sendConfirmationEmail(HealthInsurance policy) {
        try {
            emailService.sendEmailWithAttachment(
                policy.getEmailAddress(),
                "Policy Confirmation",
                policy
            );
            logger.info("Confirmation email sent successfully for policy ID: {}", policy.getPolicyId());
        } catch (MessagingException e) {
            logger.error("Failed to send confirmation email due to messaging error", e);
        } catch (IOException e) {
            logger.error("Failed to send confirmation email due to I/O error", e);
        }
    }
}
