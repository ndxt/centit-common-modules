package com.centit.support.metadata.service;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.metadata.po.MetaColumn;
import com.centit.support.metadata.po.MetaRelation;
import com.centit.support.metadata.po.MetaTable;

import javax.persistence.Column;
import java.util.List;

public interface MetaDataService {

    List<MetaTable> listMetaTables(String databaseCode, PageDesc pageDes);

    List<DatabaseInfo> listDatabase();

    List<SimpleTableInfo> listRealTables(String databaseCode);

    void syncDb(String databaseCode, String recorder);

    void updateMetaTable(String tableId, String tableName, String tableComment, String tableState, String recorder);

    MetaTable getMetaTable(String tableId);

    List<MetaRelation> listMetaRelation(String tableId, PageDesc pageDesc);

    List<MetaColumn> listMetaColumns(String tableName, PageDesc pageDesc);

    void createRelation(MetaRelation relation);

    void saveRelations(String tableId, List<MetaRelation> relations);

    MetaColumn getMetaColumn(String tableId, String columnName);

    void updateMetaColumn(MetaColumn metaColumn);

    JSONObject getMetaTableCascade(String databaseCode, String tableName);
}
