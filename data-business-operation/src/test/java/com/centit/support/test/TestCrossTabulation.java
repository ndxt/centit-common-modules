package com.centit.support.test;

import com.centit.support.data.core.DataSet;
import com.centit.support.data.core.SimpleDataSet;
import com.centit.support.data.utils.DataSetOptUtil;

import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataSetOptUtil.crossTabulation单元测试
 *
 * @author chen_l
 * @date 2019/3/7
 */
public class TestCrossTabulation extends TestCase{
    //打印日志
    Logger logger = LoggerFactory.getLogger(TestCrossTabulation.class);
    //预置数据集
    private SimpleDataSet initdataSet = null;
    //预置行头、列头
    private Map<String, List<String>> headerFieldsMap = null;
    //预期结果数据集
    private SimpleDataSet resultSetExpected = null;

    //测试excel存放路径
    private  final String path = Class.class.getClass().getResource("/").getPath()+"com\\centit\\support\\test\\TestCrossTabulation.xlsx";


    @Before
    public void setUp() throws Exception {
        super.setUp();
        ReadCrossTabulation readCrossTabulation = new ReadCrossTabulation(path);
        //获取预置数据集
        initdataSet = readCrossTabulation.getInitDataSet();
        //获取预置行头、列头
        headerFieldsMap = readCrossTabulation.getHeaderFields();
        //获取预期结果数据集
        resultSetExpected = readCrossTabulation.getResultSet();
    }

    @Test
    public void testCrossTabulation() {
        DataSet resultSetActual = DataSetOptUtil.crossTabulation(initdataSet, headerFieldsMap.get("rowHeaderFields"), headerFieldsMap.get("colHeaderFields"));
        Assert.assertEquals(resultSetExpected.getData(),resultSetActual.getData());
        System.out.println("crossTabulation方法测试通过！");
        logger.info("crossTabulation方法测试通过！");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
