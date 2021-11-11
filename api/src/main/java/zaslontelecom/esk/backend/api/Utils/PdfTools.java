package zaslontelecom.esk.backend.api.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PdfTools {

    public static void generatePDF(String inputHtml, String outputPdfPath, Settings settings) throws HandledException {
        try {
            inputHtml = cleanHtml(inputHtml);

            OutputStream out = new FileOutputStream(outputPdfPath);
            //Flying Saucer part
            ITextRenderer renderer = new ITextRenderer();
            String fontPath = settings.getPdfTemplatePath() + "Roboto-Regular.ttf";
            Path path = Paths.get(fontPath);
            if(!Files.exists(path)){
                throw new HandledException("Ne najdem fontov za generiranje PDF dokumentov.");
            }

            // Utils.saveFileFromString(outputPdfPath + ".html", inputHtml);
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocumentFromString(inputHtml);
            renderer.layout();
            renderer.createPDF(out);

            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new HandledException("Napaka pri generiranju PDF dokumentov.");
        } catch (com.lowagie.text.DocumentException e) {
            e.printStackTrace();
            throw new HandledException("Napaka pri generiranju PDF dokumentov.");
        }
    }

    private static String cleanHtml(String inputHtml) {
        inputHtml = inputHtml.replace("&nbsp;", " ");
        inputHtml = inputHtml.replaceAll("&(?!amp;)", "&amp;");
        return inputHtml;
    }

    public static void merge(List<String> filenames, String outputFile) {
        List<InputStream> list = new ArrayList<InputStream>();
        try {
            // Source pdfs
            for (String filename: filenames) {
                list.add(new FileInputStream(new File(filename)));
            }

            // Resulting pdf
            OutputStream out = new FileOutputStream(new File(outputFile));

            doMerge(list, out);
            Utils.deleteFiles(filenames);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new HandledException("Napaka pri generiranju PDF dokumentov.");
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new HandledException("Napaka pri generiranju PDF dokumentov.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new HandledException("Napaka pri generiranju PDF dokumentov.");
        }
    }

    private static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public static void generateWatermark(String pdfFile, String imageFile) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(pdfFile);
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(pdfFile + ".copy"));
        Image image = Image.getInstance(imageFile);
        image.setAbsolutePosition(380f,40f);
        image.scalePercent(8);

        for(int i=1; i<= reader.getNumberOfPages(); i++){
            PdfContentByte content = pdfStamper.getOverContent(i);
            content.addImage(image);
        }

        pdfStamper.close();
        reader.close();

        Utils.deleteFile(pdfFile);
        Utils.renameFile(pdfFile + ".copy", pdfFile);
    }

}
