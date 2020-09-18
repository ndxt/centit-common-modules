package com.centit.test;

import com.centit.support.office.OfficeToHtml;
import com.centit.support.office.OfficeToPdf;

public class TestPdf2Html {

    public static void main(String[] args) {
        //POIPptToHtmlUtils.pptToHtml("D:/diagnosis/file/temp//ppt2007.pptx", "D:/diagnosis/file/temp/", "test5.html");
        OfficeToHtml.ppt2Html("C:\\Users\\zhf\\Postman\\files\\2.pptx", "C:\\Users\\zhf\\Postman\\files\\tmp\\2.html","pptx");
    }

}
