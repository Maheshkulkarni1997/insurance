package org.safeguard.insurance.config;

import org.safeguard.insurance.enums.PolicyType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPolicyTypeConverter implements Converter<String, PolicyType> {

    @Override
    public PolicyType convert(String source) {
        for (PolicyType policyType : PolicyType.values()) {
            if (policyType.getDisplayName().equalsIgnoreCase(source)) {
                return policyType;
            }
        }
        throw new IllegalArgumentException("Invalid PolicyType value: " + source);
    }
}
