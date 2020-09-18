package com.centit.support.office;

import com.centit.support.office.utils.CommonUtils;
import com.centit.support.office.utils.PowerPointUtils;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.AbstractWordUtils;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author zhf
 */
public abstract class OfficeToPdf {
    private OfficeToPdf() {
        throw new IllegalAccessError("Utility class");
    }

    final static String DOC = "doc";
    final static String DOCX = "docx";

    public static boolean ppt2Pdf(String inPptFile, String outPdfFile, String suffix) {
        String inputFile = CommonUtils.mapWidowsPathIfNecessary(inPptFile);
        String pdfFile = CommonUtils.mapWidowsPathIfNecessary(outPdfFile);

        if ("ok".equals(PowerPointUtils.pptToPdfUseImage(inputFile, pdfFile, suffix))) {
            return true;
        }
        return false;
    }

    public static boolean word2Pdf(String inWordFile, String outPdfFile, String suffix) {
        try {
            String inputFile = CommonUtils.mapWidowsPathIfNecessary(inWordFile);
            String pdfFile = CommonUtils.mapWidowsPathIfNecessary(outPdfFile);

            if (DOCX.equalsIgnoreCase(suffix)) {
                XWPFDocument docx = new XWPFDocument(new FileInputStream(inputFile));
                PdfOptions options = PdfOptions.create();
                // 中文字体处理
                options.fontProvider((familyName, encoding, size, style, color) -> {
                    try {
                        BaseFont bfChinese = BaseFont.createFont("simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                        Font fontChinese = new Font(bfChinese, size, style, color);
                        if (familyName != null) {
                            fontChinese.setFamily(familyName);
                        }
                        return fontChinese;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });

                PdfConverter.getInstance().convert(docx, new FileOutputStream(pdfFile), options);
            }
            if (DOC.equalsIgnoreCase(suffix)) {
                HWPFDocumentCore wordDocument = AbstractWordUtils.loadDoc(new File(inputFile));
                WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    XMLHelper.getDocumentBuilderFactory().newDocumentBuilder()
                        .newDocument());
                wordToHtmlConverter.processDocument(wordDocument);
                Document doc = wordToHtmlConverter.getDocument();

                Transformer serializer = CommonUtils.createTransformer();
                serializer.transform(new DOMSource(doc),
                    new StreamResult(new File(pdfFile)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

}
