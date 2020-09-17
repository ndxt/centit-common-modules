package com.centit.support.office;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.AbstractWordUtils;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhf
 */
public abstract class OfficeToPdf {
    private OfficeToPdf() {
        throw new IllegalAccessError("Utility class");
    }

    private static Logger logger = LoggerFactory.getLogger(OfficeToPdf.class);
    public final static String DOC = "doc";
    public final static String DOCX = "docx";
    private static PDRectangle mediaBox = PDRectangle.LETTER;
    private static boolean landscape = false;
    private static boolean autoOrientation = true;
    private static boolean resize = true;

    private static void createPDFFromImages(List<ByteArrayOutputStream> imageFilenames, String targetFilePath) throws IOException {
        PDDocument doc = new PDDocument();
        for (ByteArrayOutputStream imageFileName : imageFilenames) {
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, imageFileName.toByteArray(), "");

            PDRectangle actualMediaBox = mediaBox;
            if ((autoOrientation && pdImage.getWidth() > pdImage.getHeight()) || landscape) {
                actualMediaBox = new PDRectangle(mediaBox.getHeight(), mediaBox.getWidth());
            }
            PDPage page = new PDPage(actualMediaBox);
            doc.addPage(page);

            PDPageContentStream contents = new PDPageContentStream(doc, page);
            if (resize) {
                contents.drawImage(pdImage, 0, 0, actualMediaBox.getWidth(), actualMediaBox.getHeight());
            } else {
                contents.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
            }
            contents.close();
        }
        doc.save(targetFilePath);
        doc.close();
    }

    private static String pptToPdfUseImage(String sourceFilePath, String targetFileName, String suffix) {
        File pptFile = new File(sourceFilePath);
        if (pptFile.exists()) {
            try {
                if ("ppt".equals(suffix)) {
                    List<ByteArrayOutputStream> htmlStr = toImage2003(sourceFilePath);
                    createPDFFromImages(htmlStr, targetFileName);
                    return "ok";
                } else if ("pptx".equals(suffix)) {
                    List<ByteArrayOutputStream> htmlStr = toImage2007(sourceFilePath);
                    createPDFFromImages(htmlStr, targetFileName);
                    return "ok";
                } else {
                    logger.error("ppt转换为pdf,源文件={}不是ppt文件", sourceFilePath);
                    return null;
                }

            } catch (Exception e) {
                logger.error("ppt文档转换为pdf,发生异常,源文件={},", sourceFilePath, e);
                return null;
            }
        } else {
            logger.error("ppt文档转换为html,源文件={}不存在", sourceFilePath);
            return null;
        }
    }

    private static List<ByteArrayOutputStream> toImage2007(String sourcePath) throws Exception {
        List<ByteArrayOutputStream> htmlStr = new ArrayList<>();
        FileInputStream is = new FileInputStream(sourcePath);
        XMLSlideShow ppt = new XMLSlideShow(is);
        is.close();
        Dimension pgsize = ppt.getPageSize();
        for (int i = 0; i < ppt.getSlides().size(); i++) {
            try {
                for (XSLFShape shape : ppt.getSlides().get(i).getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape tsh = (XSLFTextShape) shape;
                        for (XSLFTextParagraph p : tsh) {
                            for (XSLFTextRun r : p) {
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                // render
                ppt.getSlides().get(i).draw(graphics);
                // save the output
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(img, "png", out);
                out.close();
                htmlStr.add(out);
            } catch (Exception e) {
                logger.error("ppt转换为pdf,发生异常,源文件={}", sourcePath, e);
                System.out.println("第" + i + "张ppt转换出错");
                return null;
            }
        }
        return htmlStr;
    }

    private static List<ByteArrayOutputStream> toImage2003(String sourcePath) {
        List<ByteArrayOutputStream> htmlStr = new ArrayList<>();
        try {
            HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(sourcePath));
            Dimension pgsize = ppt.getPageSize();
            for (int i = 0; i < ppt.getSlides().size(); i++) {
                for (HSLFShape shape : ppt.getSlides().get(i).getShapes()) {
                    if (shape instanceof HSLFTextShape) {
                        HSLFTextShape tsh = (HSLFTextShape) shape;
                        for (HSLFTextParagraph p : tsh) {
                            for (HSLFTextRun r : p) {
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                // render
                ppt.getSlides().get(i).draw(graphics);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(img, "png", out);
                out.close();
                htmlStr.add(out);
            }
        } catch (Exception e) {
            logger.error("ppt转换为Pdf,发生异常,源文件={}", sourcePath, e);
            return null;
        }

        return htmlStr;
    }


    public static boolean ppt2Pdf(String inPptFile, String outPdfFile, String suffix) {
        String inputFile = inPptFile;
        String pdfFile = outPdfFile;
        if (File.separator.equals("\\")) {
            inputFile = inPptFile.replace('/', '\\');
            pdfFile = outPdfFile.replace('/', '\\');
        }
        if ("ok".equals(pptToPdfUseImage(inputFile, pdfFile, suffix))) {
            return true;
        }
        return false;
    }

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
