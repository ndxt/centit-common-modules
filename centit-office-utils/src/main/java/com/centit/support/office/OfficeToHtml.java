package com.centit.support.office;

import com.centit.support.office.commons.CommonUtils;
import com.centit.support.office.commons.PowerPointUtils;
import com.centit.support.report.ExcelTypeEnum;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    public static boolean excel2Html(String inExcelFile, String outPdfFile) throws TransformerException, IOException, ParserConfigurationException {
        String inFilePath = CommonUtils.mapWidowsPathIfNecessary(inExcelFile);
        String outFilePath = CommonUtils.mapWidowsPathIfNecessary(outPdfFile);

        HSSFWorkbook excelBook = new HSSFWorkbook();
        ExcelTypeEnum excelType = ExcelTypeEnum.checkFileExcelType(inFilePath);
        if (excelType == ExcelTypeEnum.HSSF) {
            excelBook = new HSSFWorkbook(new FileInputStream(inFilePath));
        } else if (excelType == ExcelTypeEnum.XSSF) {
            XlsxTransformXls xls = new XlsxTransformXls();
            XSSFWorkbook workbookOld = new XSSFWorkbook(inFilePath);
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
            new StreamResult(new File(outFilePath)));
        return true;
    }
}
