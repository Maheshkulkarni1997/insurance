package org.safeguard.insurance.service;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.safeguard.insurance.repository.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class HealthInsuranceService {

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private EmailService emailService;
    
    public List<HealthInsurance> getAllPolicies() {
        return healthInsuranceRepository.findAll();
    }

    public Optional<HealthInsurance> getPolicyById(Long policyId) {
        return healthInsuranceRepository.findById(policyId);
    }

   

    public HealthInsurance create(HealthInsurance healthInsurance) {
        // Save the policy
        HealthInsurance savedPolicy = healthInsuranceRepository.save(healthInsurance);

        // Send confirmation email with attachment
        try {
            emailService.sendEmailWithAttachment(
                savedPolicy.getEmailAddress(), // Assumes HealthInsurance has a method to get email
                "Policy Confirmation",
                savedPolicy
            );
        } catch (MessagingException e) {
            // Handle the exception (e.g., log it, rethrow it, etc.)
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return savedPolicy;
    }

    public void deletePolicy(Long policyId) {
        healthInsuranceRepository.deleteById(policyId);
    }
    
    public List<HealthInsurance> findPoliciesByTypeAndStatus(PolicyType policyType) {
        return healthInsuranceRepository.findAllByPolicyTypeAndIsActive(policyType, true);
    }
}
