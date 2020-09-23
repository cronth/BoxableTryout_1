/**
 *## Beispiel der Verwendung von
 * Apache PdfBox
 * Quelle: https://www.ulfdittmer.com/view?PdfBox
 * Ergänzt um eigene Kommentare.
 * Anmerkungen, die mit ## beginnen, sind von mir.
 */
import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class SimplePDFBox {

    public static void main (String[] args) throws Exception {
        String outputFileName = "Simple_PdfBox.pdf";
        if (args.length > 0)
            outputFileName = args[0];

        // Create a document
        //## Aus dem Dokumentationskommentar von PDDocument:
        //"This is the in-memory representation of the PDF document."
        //"The #close() method must be called once the document is no longer needed."
        PDDocument document = new PDDocument();
        //Erstellt eine Seite page1 und übergibt PDRectangle.A4 als Parameter für die Größe (?) mediaBox. PDRectangle.A4 ist
        //ein Rechteck mit den Maßen eines A4 Blattes. Eine mediaBox beschreibt die
        //physischen Maße einer Seite des PDF.
        // PDRectangle.LETTER and others are also possible
        PDPage page1 = new PDPage(PDRectangle.A4);
        // ## getMediaBox
        // rect can be used to get the page width and height
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);

        // Create a new font object selecting one of the PDF base fonts
        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
        PDFont fontMono = PDType1Font.COURIER;

        // Start a new content stream which will hold the content that's about to be created
        PDPageContentStream cos = new PDPageContentStream(document, page1);

        int line = 0;

        // Define a text content stream using the selected font, move the cursor and draw some text
        cos.beginText();
        cos.setFont(fontPlain, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50*(++line));
        cos.showText("Hello World");
        cos.endText();

        cos.beginText();
        cos.setFont(fontItalic, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50*(++line));
        cos.showText("Italic");
        cos.endText();

        cos.beginText();
        cos.setFont(fontBold, 12);
        cos.newLineAtOffset(100, rect.getHeight() - 50*(++line));
        cos.showText("Bold");
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.setNonStrokingColor(Color.BLUE);
        cos.newLineAtOffset(100, rect.getHeight() - 50*(++line));
        cos.showText("Monospaced blue");
        cos.endText();

        // Make sure that the content stream is closed:
        cos.close();

        PDPage page2 = new PDPage(PDRectangle.A4);
        document.addPage(page2);
        cos = new PDPageContentStream(document, page2);

        // draw a red box in the lower left hand corner
        cos.setNonStrokingColor(Color.RED);
        cos.addRect(50, 100, 100, 100);
        cos.fill();

        // add two lines of different widths
        cos.setLineWidth(1);
        cos.moveTo(200, 250);
        cos.lineTo(400, 250);
        cos.closeAndStroke();
        cos.setLineWidth(5);
        cos.moveTo(200, 300);
        cos.lineTo(400, 400);
        cos.closeAndStroke();

        // add an image
        try {
            PDImageXObject ximage = PDImageXObject.createFromFile("Simple.jpg", document);
            float scale = 0.2f; // alter this value to set the image size
            cos.drawImage(ximage, 100, 400, ximage.getWidth()*scale, ximage.getHeight()*scale);
        } catch (IOException ioex) {
            System.out.println("No image for you");
        }

        // close the content stream for page 2
        cos.close();

        // Save the results and ensure that the document is properly closed:
        document.save(outputFileName);
        document.close();
    }
}
