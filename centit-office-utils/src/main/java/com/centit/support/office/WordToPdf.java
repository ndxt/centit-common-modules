package com.centit.support.office;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.AbstractWordUtils;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author zhf
 */
public  class WordToPdf {
    private WordToPdf() {
        throw new IllegalAccessError("Utility class");
    }

    private static Log logger = LogFactory.getLog(WordToPdf.class);
    public final static String DOC = "doc";
    public final static String DOCX = "docx";

    public static boolean word2Pdf(String inWordFile, String outPdfFile, String suffix) {
        try {
            String inputFile = inWordFile;
            String pdfFile = outPdfFile;
            if (File.separator.equals("\\")) {
                inputFile = inWordFile.replace('/', '\\');
                pdfFile = outPdfFile.replace('/', '\\');
            }
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

                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(new File(pdfFile));

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                // TODO set encoding from a command argument
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.setOutputProperty(OutputKeys.METHOD, "html");
                serializer.transform(domSource, streamResult);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

}
