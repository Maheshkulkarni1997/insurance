/*
 * © 2024 SafeGuard
 * All rights reserved.
 *
 * Author: Mahesh
 *
 * REST controller for handling insurance policy requests.
 */

package org.safeguard.insurance.controller;

     
import org.safeguard.insurance.entitymodel.InsurancePolicy;
import org.safeguard.insurance.service.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {
    @Autowired
    private InsurancePolicyService service;

    @GetMapping
    public List<InsurancePolicy> getAllPolicies() {
        return service.getAllPolicies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicy> getPolicyById(@PathVariable Long id) {
        Optional<InsurancePolicy> policy = service.getPolicyById(id);
        return policy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InsurancePolicy> createPolicy(@RequestBody InsurancePolicy policy) {
        InsurancePolicy savedPolicy = service.savePolicy(policy);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPolicy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        service.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
