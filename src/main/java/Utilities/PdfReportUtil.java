package Utilities;

import com.itextpdf.html2pdf.HtmlConverter;
import java.io.File;
import java.io.IOException;

public class PdfReportUtil {
    /**
     * Converts an HTML file to a PDF file at the specified file paths.
     *
     * @param htmlFilePath the path of the source HTML file to be converted
     * @param pdfFilePath the path where the generated PDF file will be saved
     */
    public static void convertHtmlToPdf(String htmlFilePath, String pdfFilePath) {
        try {
            // Convert the HTML report to PDF
            HtmlConverter.convertToPdf(new File(htmlFilePath), new File(pdfFilePath));
            System.out.println("PDF report generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}