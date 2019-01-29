package com.centit.test;

import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.TransactionHandler;
import com.centit.support.serialno.po.OptFlowNoPool;

import java.sql.SQLException;

public class TestTransaction {
    public  static void  main(String[] args)   {
        runInTransaction();
    }

    public  static void runInTransaction()  {
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setConnUrl("jdbc:oracle:thin:@192.168.131.98:1521:orcl");
        dbc.setUsername("apprflow");
        dbc.setPassword("apprflow_123");
        /**
         * 假设这个对象是你要保存的; 如果调用 OrmDaoUtils.saveNewObject 成功，这个对象上必须有jpa注解
         * 有jpa注解就不用自己写sql语句了，否则自己写insert语句也是可以的
         */
        OptFlowNoPool flowNo = new OptFlowNoPool();
        flowNo.setOwnerCode("noowner");
        flowNo.setCodeCode("1");
        flowNo.setCodeDate(
            DatetimeOpt.convertToSqlDate(
                DatetimeOpt.truncateToYear(DatetimeOpt.currentUtilDate())));
        flowNo.setCurNo(12L);
        flowNo.setCreateDate(DatetimeOpt.currentSqlDate());

        try {
            Integer ret = TransactionHandler.executeInTransaction(dbc, (conn) -> {
                /**
                 * 这两个操作是在一个事物中的
                 */
                int t = OrmDaoUtils.deleteObject(conn, flowNo);
                System.out.println(t);
                //return t;
                /*DatabaseAccess.doExecuteSql(conn, "delete from table where a=? and b=?",
                        new Object[]{"a",5});*/
                return OrmDaoUtils.saveNewObject(conn, flowNo);
            });
            System.out.println(ret);
        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

}
