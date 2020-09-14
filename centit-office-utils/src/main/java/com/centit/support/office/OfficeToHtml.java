package com.centit.support.office;

import com.centit.support.file.FileSystemOpt;
import com.centit.support.report.ExcelTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by codefan on 2017/10/9.
 */

public  class OfficeToHtml {
    private OfficeToHtml() {
        throw new IllegalAccessError("Utility class");
    }

    private static Log logger = LogFactory.getLog(OfficeToHtml.class);
    public static boolean ppt2Html(String inPptFile, String outPdfFile,String suffix) {
        String inputFile = inPptFile;
        String pdfFile = outPdfFile;
        if(File.separator.equals("\\")){
            inputFile=inPptFile.replace('/', '\\');
            pdfFile = outPdfFile.replace('/', '\\');
        }
        String sFileName = FileSystemOpt.extractFullFileName(pdfFile);
        POIPptToHtmlUtils.pptToHtml(inputFile, pdfFile.replace(sFileName, ""), sFileName,suffix);
        return false;
    }
    public static boolean excel2Html(String inExcelFile, String outPdfFile) throws TransformerException, IOException, ParserConfigurationException {
        String inFilePath = inExcelFile;
        String outFilePath = outPdfFile;
        if(File.separator.equals("\\")){
            inFilePath=inExcelFile.replace('/', '\\');
            outFilePath = outPdfFile.replace('/', '\\');
        }
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


        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(new File(outFilePath));

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        // TODO set encoding from a command argument
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "no");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);

        return true;
    }
}
