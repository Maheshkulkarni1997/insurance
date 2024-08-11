package org.safeguard.insurance.config;

import java.io.InputStream;
import java.io.IOException;
import org.safeguard.insurance.base.BaseInsurancePolicy;

/**
 * Interface for PDF generation strategies.
 */
public interface PdfGenerationInterface {
    InputStream generatePdf(BaseInsurancePolicy insurancePolicy) throws IOException;
}
