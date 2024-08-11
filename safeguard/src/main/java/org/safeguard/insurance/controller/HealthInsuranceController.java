package org.safeguard.insurance.controller;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.safeguard.insurance.service.HealthInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class HealthInsuranceController {

    @Autowired
    private HealthInsuranceService healthInsuranceService;

    @GetMapping("/api/v1/health-insurance")
    public List<HealthInsurance> getAllPolicies() {
        return healthInsuranceService.getAllPolicies();
    }

    @GetMapping("/api/v1/health-insurance/{id}")
    public ResponseEntity<HealthInsurance> getPolicyById(@PathVariable("id") Long id) {
        Optional<HealthInsurance> healthInsurance = healthInsuranceService.getPolicyById(id);
        return healthInsurance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/v1/health-insurance")
    public HealthInsurance createPolicy(@Valid @RequestBody HealthInsurance healthInsurance) {
        return healthInsuranceService.create(healthInsurance);
    }

    @PutMapping("/api/v1/health-insurance/{id}")
    public ResponseEntity<HealthInsurance> updatePolicy(@PathVariable("id") Long id, @RequestBody HealthInsurance healthInsurance) {
        Optional<HealthInsurance> existingPolicy = healthInsuranceService.getPolicyById(id);
        if (existingPolicy.isPresent()) {
            healthInsurance.setPolicyId(id);
            return ResponseEntity.ok(healthInsuranceService.create(healthInsurance));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/v1/health-insurance/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable("id") Long id) {
        if (healthInsuranceService.getPolicyById(id).isPresent()) {
            healthInsuranceService.deletePolicy(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/api/v1/health-insurance/policytypes")
    public ResponseEntity<List<HealthInsurance>> getPoliciesByTypeAndStatus(
            @RequestParam("policyType") PolicyType policyType) {
        List<HealthInsurance> policies = healthInsuranceService.findPoliciesByTypeAndStatus(policyType);
        return ResponseEntity.ok(policies);
    }
}


