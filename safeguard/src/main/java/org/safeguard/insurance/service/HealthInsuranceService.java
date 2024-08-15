package org.safeguard.insurance.service;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.safeguard.insurance.repository.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
            // Check and set the policy date if it's null
            if (healthInsurance.getPolicyDate() == null) {
                healthInsurance.setPolicyDate(LocalDate.now()); // Set to current date if not provided
            }

            // Generate a unique policy number
            StringBuilder policyNumberBuilder = new StringBuilder();
            policyNumberBuilder.append(healthInsurance.getPolicyType().name()); // Use name() to get string representation of enum

            // Format policy date in yyyyMMdd format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedDate = healthInsurance.getPolicyDate().format(formatter);
            policyNumberBuilder.append('-').append(formattedDate);

            // Set the policy number to the healthInsurance object
            healthInsurance.setPolicyNumber(policyNumberBuilder.toString());

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
                // Handle the exception
                e.printStackTrace();
            }

            savedPolicy.setIsActive(true);
            return savedPolicy;
        }
    

    public void deletePolicy(Long policyId) {
        healthInsuranceRepository.deleteById(policyId);
    }
    
    public List<HealthInsurance> findPoliciesByTypeAndStatus(PolicyType policyType) {
        return healthInsuranceRepository.findAllByPolicyTypeAndIsActive(policyType, true);
    }
}
