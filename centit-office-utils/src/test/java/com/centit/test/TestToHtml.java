package com.centit.test;

import com.centit.support.office.OfficeToHtml;

public class TestToHtml {

    public static void main(String[] args) {
        OfficeToHtml.word2Html("/D/Projects/RunData/demo_home/temp/case6.docx",
            "/D/Projects/RunData/demo_home/temp/case6.html");
        OfficeToHtml.word2Html("/D/Projects/RunData/demo_home/temp/case7.doc",
            "/D/Projects/RunData/demo_home/temp/case7.html");
        //POIPptToHtmlUtils.pptToHtml("D:/diagnosis/file/temp//ppt2007.pptx", "D:/diagnosis/file/temp/", "test5.html");
       // OfficeToHtml.ppt2Html("C:\\Users\\zhf\\Postman\\files\\2.pptx", "C:\\Users\\zhf\\Postman\\files\\tmp\\2.html","pptx");
    }

}
