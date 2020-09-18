package com.centit.support.office;

import com.centit.support.file.FileType;
import com.centit.support.office.commons.CommonUtils;
import com.centit.support.office.commons.PowerPointUtils;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Created by codefan on 2017/10/9.
 */

public abstract class OfficeToHtml {
    private static Logger logger = LoggerFactory.getLogger(OfficeToHtml.class);

    private OfficeToHtml() {
        throw new IllegalAccessError("Utility class");
    }

    public static boolean ppt2Html(String inPptFile, String outPdfFile, String suffix) {
        String inputFile = CommonUtils.mapWidowsPathIfNecessary(inPptFile);
        String pdfFile = CommonUtils.mapWidowsPathIfNecessary(outPdfFile);

        if ("ok".equals(PowerPointUtils.pptToHtmlUseImage(inputFile, pdfFile, suffix))) {
            return true;
        }
        return false;
    }

    public static boolean excel2Html(InputStream inExcelStream, OutputStream outPdfStream, String suffix) throws TransformerException, IOException, ParserConfigurationException {

        HSSFWorkbook excelBook = new HSSFWorkbook();
        if("xls".equalsIgnoreCase(suffix)){
            excelBook = new HSSFWorkbook(inExcelStream);
        } else if("xlsx".equalsIgnoreCase(suffix)){
            XlsxTransformXls xls = new XlsxTransformXls();
            XSSFWorkbook workbookOld = new XSSFWorkbook(inExcelStream);
            xls.transformXSSF(workbookOld, excelBook);
        } else {
            return false;
        }

        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        excelToHtmlConverter.setOutputColumnHeaders(false);
        //去掉Excel行号
        excelToHtmlConverter.setOutputRowNumbers(false);
        excelToHtmlConverter.processWorkbook(excelBook);
        Document htmlDocument = excelToHtmlConverter.getDocument();
        Transformer serializer = CommonUtils.createTransformer();
        serializer.transform(new DOMSource(htmlDocument),
            new StreamResult(outPdfStream));
        return true;
    }

    public static boolean excel2Html(String inExcelFile, String outPdfFile, String suffix) throws TransformerException, IOException, ParserConfigurationException {
        String inFilePath = CommonUtils.mapWidowsPathIfNecessary(inExcelFile);
        String outFilePath = CommonUtils.mapWidowsPathIfNecessary(outPdfFile);

        try(InputStream inWordStream = new FileInputStream(new File(inFilePath));
            OutputStream outPdfStram = new FileOutputStream(new File(outFilePath))) {
            return excel2Html(inWordStream, outPdfStram, suffix);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }

    }

    public static boolean excel2Html(String inExcelFile, String outPdfFile) throws TransformerException, IOException, ParserConfigurationException {
        return excel2Html(inExcelFile, outPdfFile, FileType.getFileExtName(inExcelFile));
    }
}
