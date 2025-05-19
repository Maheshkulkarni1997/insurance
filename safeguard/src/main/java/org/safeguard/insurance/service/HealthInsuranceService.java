package org.safeguard.insurance.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.safeguard.insurance.exception.ApplicationException;
import org.safeguard.insurance.exception.DuplicateEntityFoundException;
import org.safeguard.insurance.repository.HealthInsuranceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

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
	public HealthInsurance getPolicyById(Long id) {

		HealthInsurance hi = healthInsuranceRepository.findByIdAndIsActive(id, true);

		if (hi == null) {
			throw new ApplicationException("The policy with ID " + id + " was not found.");
		}

		return hi;
	}

	@Transactional
	public HealthInsurance create(HealthInsurance healthInsurance) {
		logger.info("Creating new health insurance policy");

		Long id = healthInsurance.getId();

		if (id != null && healthInsuranceRepository.existsById(id)) {
			throw new DuplicateEntityFoundException("The given policy is alredy exist");
		}

		if (healthInsurance.getPolicyDate() == null) {
			healthInsurance.setPolicyDate(LocalDate.now());
			logger.debug("Policy date not provided, using current date");
		}

		String policyNumber = generatePolicyNumber(healthInsurance);
		healthInsurance.setPolicyNumber(policyNumber);
		logger.debug("Generated policy number: {}", policyNumber);

		HealthInsurance savedPolicy = healthInsuranceRepository.save(healthInsurance);
		logger.info("Successfully saved health insurance policy with ID: {}", savedPolicy.getId());

		sendConfirmationEmail(savedPolicy);

		healthInsurance.setIsActive(true);

		return savedPolicy;
	}

	@Transactional
	public HealthInsurance update(Long id, HealthInsurance healthInsurance) {
		logger.info("Updating health insurance policy with ID: {}", id);
		HealthInsurance existingPolicy = this.getPolicyById(id);
		if (id == null && !healthInsuranceRepository.existsById(id)) {
			throw new EntityNotFoundException("The given id was not found" + id);
		}
		HealthInsurance updatedPolicy = healthInsuranceRepository.save(existingPolicy);
		return updatedPolicy;
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

		// Get the policy type (e.g., "HEALTH", "LIFE", etc.)
		policyNumberBuilder.append(healthInsurance.getPolicyType().name());
		policyNumberBuilder.append('-');

		// Format the policy date (e.g., "2023-05-17")
		String formattedDate = healthInsurance.getPolicyDate().format(POLICY_DATE_FORMATTER);
		policyNumberBuilder.append(formattedDate);

		// Generate a UUID and append it to make the policy number globally unique
		String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8); // 8-character UUID suffix
		policyNumberBuilder.append('-').append(uniqueSuffix);

		return policyNumberBuilder.toString();
	}

	private void sendConfirmationEmail(HealthInsurance policy) {
		try {
			emailService.sendEmailWithAttachment(policy.getEmailAddress(), "Policy Confirmation", policy);
			logger.info("Confirmation email sent successfully for policy ID: {}", policy.getId());
		} catch (MessagingException e) {
			logger.error("Failed to send confirmation email due to messaging error", e);
		} catch (IOException e) {
			logger.error("Failed to send confirmation email due to I/O error", e);
		}
	}
}
