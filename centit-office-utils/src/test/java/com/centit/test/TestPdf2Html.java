package com.centit.test;

import com.centit.support.office.OfficeToHtml;
import com.centit.support.office.OfficeToPdf;

public class TestPdf2Html {

    public static void main(String[] args) {
        //POIPptToHtmlUtils.pptToHtml("D:/diagnosis/file/temp//ppt2007.pptx", "D:/diagnosis/file/temp/", "test5.html");
        OfficeToPdf.ppt2Pdf("C:\\Users\\zhf\\Postman\\files\\1.pptx", "C:\\Users\\zhf\\Postman\\files\\tmp\\2.pdf","pptx");
    }

}
