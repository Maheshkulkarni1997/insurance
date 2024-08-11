package org.safeguard.insurance.repository;


import java.util.List;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Long> {
	
    List<HealthInsurance> findAllByPolicyTypeAndIsActive(PolicyType policyType, Boolean isActive);

}
