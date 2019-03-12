package com.centit.support.report;

import java.util.Map;

/**
 * Created by codefan on 17-9-23.
 */
public class TestExcelImport {

    public static void main(String[] args) throws Exception {
        //"/home/codefan/temp/2018.03.01-2018.04.30.xlsx",
        // "/home/codefan/下载/北京.xlsx"
        LargeExcelImportUtil.parserXSSFSheet("/home/codefan/temp/2018.03.01-2018.04.30.xlsx","3",0,
            (rowMap)-> {
                for (Map.Entry<Integer, Object> ent : rowMap.entrySet()) {
                    System.out.print(ent.getValue());
                    System.out.print("\t");
                }
                System.out.println();
            }
        );
    }

}
