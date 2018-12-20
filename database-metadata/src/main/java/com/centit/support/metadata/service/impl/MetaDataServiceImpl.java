package com.centit.support.metadata.service.impl;

import com.centit.framework.common.ObjectException;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.metadata.*;
import com.centit.support.metadata.dao.MetaColumnDao;
import com.centit.support.metadata.dao.MetaTableDao;
import com.centit.support.metadata.po.MetaColumn;
import com.centit.support.metadata.po.MetaTable;
import com.centit.support.metadata.service.MetaDataService;
import com.centit.support.metadata.utils.JdbcConnect;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class MetaDataServiceImpl implements MetaDataService {
    private Logger logger = LoggerFactory.getLogger("元数据");

    @Autowired
    private IntegrationEnvironment integrationEnvironment;

    @Autowired
    private MetaTableDao metaTableDao;

    @Autowired
    private MetaColumnDao metaColumnDao;

    @Override
    public List<DatabaseInfo> listDatabase() {
        return integrationEnvironment.listDatabaseInfo();
    }

    @Override
    public List<MetaTable> listMetaTables(String databaseCode) {
        DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
            JdbcMetadata jdbcMetadata = new JdbcMetadata();
            try {
                jdbcMetadata.setDBConfig(JdbcConnect.getConn(databaseInfo));
            }catch (SQLException e){
                logger.error("连接数据库【{}】出错",databaseInfo.getDatabaseName());
            }
        return null;
    }

    @Override
    public List<SimpleTableInfo> listRealTables(String databaseCode) {
        DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
        JdbcMetadata jdbcMetadata = new JdbcMetadata();
        try {
            jdbcMetadata.setDBConfig(JdbcConnect.getConn(databaseInfo));
        }catch (SQLException e){
            logger.error("连接数据库【{}】出错",databaseInfo.getDatabaseName());
            throw new ObjectException("连接数据库出错");
        }
        return jdbcMetadata.listAllTable();
    }

    public void syncDb(String databaseCode){
        DatabaseInfo dbInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
        List<SimpleTableInfo> dbTables = listRealTables(databaseCode);
        List<MetaTable> metaTables = metaTableDao.listObjectsByFilter("where DATABASE_CODE = ?", new Object[]{databaseCode});
        Comparator<TableInfo> comparator = (o1, o2) -> StringUtils.compare(o1.getTableName(), o2.getTableName());
        Triple<List<SimpleTableInfo>, List<Pair<MetaTable, SimpleTableInfo>>, List<MetaTable>> triple = compareMetaBetweenDbTables(metaTables, dbTables, comparator);
        if(triple.getLeft() != null && triple.getLeft().size() > 0){
            //新增
            for(SimpleTableInfo table : triple.getLeft()){
                //表
                MetaTable metaTable = new MetaTable().convertFromDbTable(table);
                metaTable.setDatabaseCode(databaseCode);
                metaTable.setTableType("T");//表
                metaTableDao.saveNewObject(metaTable);
                //列
                List<SimpleTableField> columns = table.getColumns();
                for(SimpleTableField tableField : columns){
                    MetaColumn column = dbCol2MetaCol(tableField);
                    column.setTableId(metaTable.getTableId());
                    metaColumnDao.saveNewObject(column);
                }
            }
        }
        if(triple.getRight() != null && triple.getRight().size() > 0) {
            //删除
            for (MetaTable table : triple.getRight()) {
                metaTableDao.deleteObjectReferences(table);
                metaTableDao.deleteObject(table);
            }
        }
        if(triple.getMiddle() != null && triple.getMiddle().size() > 0){
            //更新
            for(Pair<MetaTable, SimpleTableInfo> pair : triple.getMiddle()){
                MetaTable oldTable = pair.getLeft();
                SimpleTableInfo newTable = pair.getRight();
                //表
                metaTableDao.updateObject(oldTable.convertFromDbTable(newTable));
                //列
                List<MetaColumn> oldColumn = oldTable.getColumns();
                List<SimpleTableField> newColumns = newTable.getColumns();
                Comparator<TableField> columnComparator = (o1, o2) -> StringUtils.compare(o1.getColumnName(), o2.getColumnName());
                Triple<List<SimpleTableField>, List<Pair<MetaColumn, SimpleTableField>>, List<MetaColumn>> columnCompared = compareMetaBetweenDbTables(oldColumn, newColumns, columnComparator);
                if(columnCompared.getLeft() != null && columnCompared.getLeft().size() > 0){
                    //新增
                    for(SimpleTableField tableField : columnCompared.getLeft()){

                    }
                }
                if(columnCompared.getRight() != null && columnCompared.getRight().size() > 0){
                    //删除
                    for(MetaColumn metaColumn : columnCompared.getRight()){

                    }
                }
                if(columnCompared.getMiddle() != null && columnCompared.getMiddle().size() > 0){
                    //更新
                    for(Pair<MetaColumn, SimpleTableField> columnPair : columnCompared.getMiddle()){

                    }
                }
            }
        }
    }

    @Override
    public void syncTable(String databaseCode, String tableId) {
        DatabaseInfo dbInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
        MetaTable metaTable = metaTableDao.getObjectWithReferences(tableId);
    }

    private <K,V> Triple<List<K>, List<Pair<V, K>>, List<V>>
            compareMetaBetweenDbTables(List<V> metaTables, List<K> simpleTableInfos, Comparator comparator){
        if(metaTables==null ||metaTables.size()==0)
            return new ImmutableTriple<> (
                simpleTableInfos,null,null);
        if(simpleTableInfos==null ||simpleTableInfos.size()==0)
            return new ImmutableTriple<> (
                null,null,metaTables);
        List<V> oldList = CollectionsOpt.cloneList(metaTables);
        List<K> newList = CollectionsOpt.cloneList(simpleTableInfos);
        Collections.sort(oldList, comparator);
        Collections.sort(newList, comparator);
        //---------------------------------------
        int i=0; int sl = oldList.size();
        int j=0; int dl = newList.size();
        List<K> insertList = new ArrayList<>();
        List<V> delList = new ArrayList<>();
        List<Pair<V,K>> updateList = new ArrayList<>();
        while(i<sl&&j<dl){
            int n = comparator.compare(oldList.get(i), newList.get(j));
            if(n<0){
                delList.add(oldList.get(i));
                i++;
            }else if(n==0){
                updateList.add( new ImmutablePair<>(oldList.get(i),newList.get(j)));
                i++;
                j++;
            }else {
                insertList.add(newList.get(j));
                j++;
            }
        }

        while(i<sl){
            delList.add(oldList.get(i));
            i++;
        }

        while(j<dl){
            insertList.add(newList.get(j));
            j++;
        }

        return new ImmutableTriple<>(insertList,updateList,delList);
    }

    private MetaColumn dbCol2MetaCol(SimpleTableField tableField){
        MetaColumn column = new MetaColumn();
        column.setColumnName(tableField.getColumnName());
        column.setFieldLabelName(tableField.getFieldLabelName());
        column.setColumnComment(tableField.getColumnComment());
        column.setColumnFieldType(tableField.getColumnType());
        column.setMaxLength(tableField.getMaxLength());
        column.setScale(tableField.getScale());
        column.setAccessType(tableField.getColumnType());
        return column;
    }

}
