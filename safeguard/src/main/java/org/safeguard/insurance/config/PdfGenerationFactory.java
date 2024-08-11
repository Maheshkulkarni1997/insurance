package org.safeguard.insurance.config;

import org.safeguard.insurance.base.BaseInsurancePolicy;
import org.safeguard.insurance.entitymodel.HealthInsurance;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to retrieve the appropriate PDF generation strategy based on the insurance policy type.
 */
public class PdfGenerationFactory {
    private static final Map<Class<? extends BaseInsurancePolicy>, PdfGenerationInterface> strategies = new HashMap<>();

    static {
        strategies.put(HealthInsurance.class, new HealthInsurancePdfReportGenerator());
        // Register other strategies here
    }

    /**
     * Retrieve the appropriate strategy based on the insurance policy type.
     * 
     * @param insurancePolicy the insurance policy for which PDF is to be generated
     * @return the corresponding PDF generation strategy
     */
    public static PdfGenerationInterface getStrategy(BaseInsurancePolicy insurancePolicy) {
        return strategies.get(insurancePolicy.getClass());
    }
}
