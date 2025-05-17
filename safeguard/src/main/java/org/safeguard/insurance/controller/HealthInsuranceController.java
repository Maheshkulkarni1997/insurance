package org.safeguard.insurance.controller;

import java.util.List;

import org.safeguard.insurance.entitymodel.HealthInsurance;
import org.safeguard.insurance.enums.PolicyType;
import org.safeguard.insurance.exception.ActionValidationGroups;
import org.safeguard.insurance.service.HealthInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@Validated
public class HealthInsuranceController {

	@Autowired
	private HealthInsuranceService healthInsuranceService;

	@GetMapping("/api/healthinsurance")
	public List<HealthInsurance> getAllPolicies() {
		return healthInsuranceService.getAllPolicies();
	}

	@GetMapping("/api/healthinsurance/{id}")
	public ResponseEntity<HealthInsurance> getPolicyById(@PathVariable("id") Long id) {
		HealthInsurance healthInsurance = healthInsuranceService.getPolicyById(id);
		return ResponseEntity.ok(healthInsurance);
	}

	@Validated(ActionValidationGroups.Create.class)
	@PostMapping("/api/healthinsurance")
	public ResponseEntity<HealthInsurance> createPolicy(@Valid @RequestBody HealthInsurance healthInsurance) {
		HealthInsurance createdHealthInsurance = healthInsuranceService.create(healthInsurance);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdHealthInsurance);
	}

	@Validated(ActionValidationGroups.Update.class)
	@PutMapping("/api/healthinsurance/{id}")
	public ResponseEntity<HealthInsurance> updatePolicy(@PathVariable("id") Long id,
			@RequestBody HealthInsurance healthInsurance) {
		HealthInsurance createdHealthInsurance = healthInsuranceService.update(id, healthInsurance);
		return ResponseEntity.status(HttpStatus.OK).body(createdHealthInsurance);
	}

	@DeleteMapping("/api/healthinsurance/{id}")
	public ResponseEntity<Void> deletePolicy(@PathVariable("id") Long id) {
		healthInsuranceService.deletePolicy(id);
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/api/healthinsurance/policytypes")
	public ResponseEntity<List<HealthInsurance>> getPoliciesByTypeAndStatus(
			@RequestParam("policyType") PolicyType policyType) {
		List<HealthInsurance> policies = healthInsuranceService.findPoliciesByTypeAndStatus(policyType);
		return ResponseEntity.ok(policies);
	}
}
