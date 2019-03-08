package com.centit.support.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取excel
 *
 * @author chen_l
 * @date 2019/3/7
 */
public class ReadExcel {

    //单元格的值出现公式时使用
    private static FormulaEvaluator evaluator;

    /**
     * 获取单元格各类型值，返回字符串类型
     *
     * @param path excel文件的路径
     * @return dataMap excel文件的内容，格式{sheetName1:[[row1cell1,row1cell2,row1cell3],[row2cell1,row2cell2,row2cell3]],sheetName2:[[row1cell1,row1cell2,row1cell3],[row2cell1,row2cell2,row2cell3]]}
     */
    public Map<String, List<List<String>>> getDataMap(String path) {
        Map<String, List<List<String>>> dataMap = new HashMap<String, List<List<String>>>();//存放excel数据
        try {
            File excelFile = new File(path);
            FileInputStream in = new FileInputStream(excelFile); // 文件流
            XSSFWorkbook wb = new XSSFWorkbook(in);
            int sheetNum = wb.getNumberOfSheets();//获取sheet页总数
            for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
                Sheet sheet = wb.getSheetAt(sheetIndex);//获取sheet页
                String sheetName = sheet.getSheetName();//获取sheet名称，在map中作为键使用
                int rowNum = sheet.getLastRowNum();//获取sheet中数据的总行数，遍历行获取数据
                List<List<String>> dataList = new ArrayList<List<String>>();//存放sheet页数据，一行数据是一个List<String>
                for (Row row : sheet) {
                    List<String> valueList = new ArrayList<String>();//存放行数据
                    for (Cell cell : row) {
                        valueList.add(getCellValueByCell(cell));//将单元格的值存放到行数据中
                    }
                    dataList.add(valueList);//将行数据存放到sheet数据中
                }
                dataMap.put(sheetName, dataList);//将sheet数据存放到excel数据中
            }
            in.close();//关闭文件流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    /**
     * 获取单元格各类型值，返回字符串类型
     *
     * @param cell 单元格
     * @return 单元格的值（String类型）
     */
    private String getCellValueByCell(Cell cell) {
        //判断是否为null或空串
        if (cell == null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_FORMULA) { //表达式类型
            cellType = evaluator.evaluate(cell).getCellType();
        }
        switch (cellType) {
            case Cell.CELL_TYPE_STRING: //字符串类型
                cellValue = cell.getStringCellValue().trim();
                cellValue = StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            case Cell.CELL_TYPE_BOOLEAN:  //布尔类型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC: //数值类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                    cellValue = DateFormat.getDateTimeInstance().format(cell.getDateCellValue());
                } else {  //否
                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            default: //其它类型，取空串
                cellValue = "";
                break;
        }
        return cellValue;
    }
}
