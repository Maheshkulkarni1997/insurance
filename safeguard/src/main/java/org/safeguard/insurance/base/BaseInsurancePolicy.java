package org.safeguard.insurance.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.safeguard.insurance.enums.PolicyType;

/**
 * Abstract base class for insurance policies.
 */
@SuppressWarnings("serial")
public abstract class BaseInsurancePolicy extends BaseEntityModel implements Serializable {
    public abstract String getPolicyNumber();
    public abstract String getPolicyHolderName();
    public abstract String getCoverageDetails();
    public abstract BigDecimal getPremiumAmount();
    public abstract BigDecimal getDeductible();
    public abstract String getNetworkList();
    public abstract String getClaimProcessDetails();
    public abstract String getExclusions();
    public abstract PolicyType getPolicyType();
    public abstract LocalDate getExpiryDate();
    public abstract LocalDate getNextPremiumDate();
}
