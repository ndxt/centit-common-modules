package com.centit.test;

import com.centit.support.office.OfficeToHtml;

public class TestPdf2Html {

    public static void main(String[] args) {
        //POIPptToHtmlUtils.pptToHtml("D:/diagnosis/file/temp//ppt2007.pptx", "D:/diagnosis/file/temp/", "test5.html");
        OfficeToHtml.ppt2Html("C:\\Users\\zhf\\Postman\\files\\1.pptx", "C:\\Users\\zhf\\Postman\\files\\tmp","ppt");
    }

}
