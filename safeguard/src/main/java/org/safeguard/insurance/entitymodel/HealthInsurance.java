package org.safeguard.insurance.entitymodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.safeguard.insurance.base.BaseInsurancePolicy;
import org.safeguard.insurance.enums.PolicyType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "health_insurance")
@Data
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = false)
public class HealthInsurance extends BaseInsurancePolicy implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "policy_id")
	private Long policyId;

	@Column(name = "policy_number", nullable = false, unique = true)
	@Size(max = 255, message = "Policy number cannot exceed 255 characters.")
	private String policyNumber;

	@Column(name = "email_address", nullable = false, unique = true)
	@Size(max = 100, message = "Email Address cannot exceed 100 characters.")
	private String emailAddress;

	@Column(name = "Mobile_Number")
	private Long mobileNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "policy_type", nullable = false)
	private PolicyType policyType;

	@Column(name = "policy_holder_name", nullable = false)
	private String policyHolderName;

	@Lob
	@Column(name = "coverage_details")
	private String coverageDetails;

	@Column(name = "premium_amount", precision = 10, scale = 2)
	private BigDecimal premiumAmount;

	@Column(name = "deductible", precision = 10, scale = 2)
	private BigDecimal deductible;

	@Lob
	@Column(name = "network_list")
	private String networkList;

	@Lob
	@Column(name = "claim_process_details")
	private String claimProcessDetails;

	@Lob
	@Column(name = "exclusions")
	private String exclusions;

	@Column(name = "expiry_date")
	private LocalDate expiryDate;

	@Column(name = "next_premium_date")
	private LocalDate nextPremiumDate;

	@Column(name = "policy_date")
	private LocalDate policyDate;

}
