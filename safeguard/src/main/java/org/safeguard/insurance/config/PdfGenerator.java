package org.safeguard.insurance.config;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.safeguard.insurance.base.BaseInsurancePolicy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Base class for generating PDF reports for various insurance policies.
 */
public abstract class PdfGenerator implements PdfGenerationInterface {

    @Override
    public InputStream generatePdf(BaseInsurancePolicy insurancePolicy) throws IOException {
        // Extract policy data as a map
        Map<String, String> data = extractPolicyData(insurancePolicy);

        // Create a new PDF document
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            // Set up content stream for writing text
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float margin = 25;
                float yPosition = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float rowHeight = 20;
                float tableTopY = yPosition - 20;
                int cols = 2;

                // Draw table headers
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, tableTopY);
                contentStream.showText("Field");
                contentStream.newLineAtOffset(tableWidth / cols, 0);
                contentStream.showText("Value");
                contentStream.endText();

                // Draw table rows with policy data
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, tableTopY - rowHeight);
                    contentStream.showText(entry.getKey());
                    contentStream.newLineAtOffset(tableWidth / cols, 0);
                    contentStream.showText(entry.getValue());
                    contentStream.endText();
                    tableTopY -= rowHeight;
                }
            }

            // Save document to output stream and return as InputStream
            document.save(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    /**
     * Extracts policy data from a given insurance policy.
     * 
     * @param insurancePolicy the insurance policy to extract data from
     * @return a map of field names to their values
     */
    protected abstract Map<String, String> extractPolicyData(BaseInsurancePolicy insurancePolicy);
}
