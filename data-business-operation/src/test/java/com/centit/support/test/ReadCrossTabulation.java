package com.centit.support.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.centit.support.data.core.SimpleDataSet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取crossTabulation方法的单元测试数据
 *
 * @author chen_l
 * @date 2019/3/6
 */
public class ReadCrossTabulation {
    //excel
    private XSSFWorkbook workbook = null;

    public ReadCrossTabulation(String path) {
        try {
            File excelFile = new File(path);
            FileInputStream in = new FileInputStream(excelFile); // 文件流
            workbook = new XSSFWorkbook(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取预置数据（数据集，读取Sheet页"initData"表格，表头为键，每行数据为一组map数据）
     *
     * @param
     * @return SimpleDataSet initDataSet 返回数据集，单元测试时赋值给crossTabulation方法的参数inData
     */
    public SimpleDataSet getInitDataSet() {
        SimpleDataSet initDataSet = new SimpleDataSet();
        //获取Sheet页"initData"
        Sheet sheetInitData = workbook.getSheet("initData");
        List<Map<String, Object>> dataList = getDataList(sheetInitData);
        //System.out.println(dataList);
        //将dataList放入dataSet中
        initDataSet.setData(dataList);
        return initDataSet;
    }

    /**
     * 获取预置数据（行头，列头）
     *
     * @param
     * @return Map<String, List < String>> headerFieldsMap（{rowHeaderFields:[],colHeaderFields:[]}），单元测试时赋值给crossTabulation方法的参数rowHeaderFields，colHeaderFields
     */
    public Map<String, List<String>> getHeaderFields() {
        Map<String, List<String>> headerFieldsMap = new HashMap<String, List<String>>();
        //获取Sheet页"HeaderFields"
        Sheet sheetHeaderFields = workbook.getSheet("HeaderFields");
        //按行获取行头（rowHeaderFields）和列头（colHeaderFields）
        for (int i = 0; i <= sheetHeaderFields.getLastRowNum(); i++) {
            //获取key
            Row row = sheetHeaderFields.getRow(i);
            String header = row.getCell(0).getStringCellValue();
            //获取value
            List<String> fields = new ArrayList<String>();
            int columnNum = row.getLastCellNum();
            for (int j = 1; j < columnNum; j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    //将cell的单元格数据类型转换为String类型
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    fields.add(cell.getStringCellValue());
                }
            }
            headerFieldsMap.put(header, fields);
        }
//        System.out.println(headerFieldsMap);
        return headerFieldsMap;
    }

    /**
     * 获取预期结果数据（数据集，读取Sheet页"result"表格，表头为键，每行数据为一组map数据）
     *
     * @param
     * @return SimpleDataSet dataSet 返回数据集，单元测试时与crossTabulation方法的返回值进行比较
     */
    public SimpleDataSet getResultSet() {
        SimpleDataSet dataSet = new SimpleDataSet();
        //获取Sheet页"HeaderFields"
        Sheet sheetResult = workbook.getSheet("result");
        List<Map<String, Object>> resultList = getDataList(sheetResult);
//        System.out.println(resultList);
        //将resultList放入dataSet中
        dataSet.setData(resultList);
        return dataSet;
    }

    /**
     * 获取excel数据（表头为键，每行数据为一组map数据，放入list中）
     *
     * @param
     * @return 返回list
     */
    private List<Map<String, Object>> getDataList(Sheet sheet) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //总行数
        int rowNum = sheet.getLastRowNum();
        //总列数
        int columnNum = sheet.getRow(0).getLastCellNum();
        //获取表头作为map中的键
        List<String> keyList = new ArrayList<String>();
        Row row0 = sheet.getRow(0);
        for (int i = 0; i < columnNum; i++) {
            keyList.add(row0.getCell(i).getStringCellValue());
        }
        //按行依次读取单元格的数据，每行数据组成一个map，将map放入list中
        for (int i = 1; i <= rowNum; i++) {
            Map<String, Object> data = new HashMap<String, Object>();
            Row row = sheet.getRow(i);
            for (int j = 0; j < columnNum; j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    //将cell的单元格数据类型转换为String类型
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    data.put(keyList.get(j), cell.getStringCellValue());
                }
            }
            //将每行数据读取的map放入list中
            dataList.add(data);
        }
        return dataList;
    }

}
