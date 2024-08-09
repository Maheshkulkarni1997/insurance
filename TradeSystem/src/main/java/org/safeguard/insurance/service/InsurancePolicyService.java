/*
 * © 2024 SafeGuard
 * All rights reserved.
 *
 * Author: Mahesh
 *
 * Service class for managing insurance policies.
 */

package org.safeguard.insurance.service;

import java.util.List;
import java.util.Optional;

import org.safeguard.insurance.entitymodel.InsurancePolicy;
import org.safeguard.insurance.repository.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsurancePolicyService {
    @Autowired
    private InsurancePolicyRepository repository;

    public List<InsurancePolicy> getAllPolicies() {
        return repository.findAll();
    }

    public Optional<InsurancePolicy> getPolicyById(Long id) {
        return repository.findById(id);
    }

    public InsurancePolicy savePolicy(InsurancePolicy policy) {
        return repository.save(policy);
    }

    public void deletePolicy(Long id) {
        repository.deleteById(id);
    }
}
