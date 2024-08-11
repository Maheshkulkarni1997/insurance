package org.safeguard.insurance.config;

import org.safeguard.insurance.base.BaseInsurancePolicy;
import org.safeguard.insurance.entitymodel.HealthInsurance;

import java.util.HashMap;
import java.util.Map;

/**
 * PDF report generator specifically for HealthInsurance policies.
 */
public class HealthInsurancePdfReportGenerator extends PdfGenerator {

    @Override
    protected Map<String, String> extractPolicyData(BaseInsurancePolicy insurancePolicy) {
        if (!(insurancePolicy instanceof HealthInsurance)) {
            throw new IllegalArgumentException("Expected HealthInsurance type");
        }
        HealthInsurance healthInsurance = (HealthInsurance) insurancePolicy;

        Map<String, String> data = new HashMap<>();
        data.put("Policy Number:", healthInsurance.getPolicyNumber());
        data.put("Policy Holder Name:", healthInsurance.getPolicyHolderName());
        data.put("Coverage Details:", healthInsurance.getCoverageDetails());
        data.put("Premium Amount:", healthInsurance.getPremiumAmount().toString());
        data.put("Deductible:", healthInsurance.getDeductible().toString());
        data.put("Network List:", healthInsurance.getNetworkList());
        data.put("Claim Process Details:", healthInsurance.getClaimProcessDetails());
        data.put("Exclusions:", healthInsurance.getExclusions());
        data.put("Policy Type:", healthInsurance.getPolicyType().toString());
        data.put("Expiry Date:", healthInsurance.getExpiryDate().toString());
        data.put("Next Premium Date:", healthInsurance.getNextPremiumDate().toString());
        return data;
    }

	
}
