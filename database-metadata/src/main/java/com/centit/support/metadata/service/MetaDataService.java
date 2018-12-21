package com.centit.support.metadata.service;

import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.metadata.po.MetaTable;

import java.util.List;

public interface MetaDataService {

    List<MetaTable> listMetaTables(String databaseCode);

    List<DatabaseInfo> listDatabase();

    List<SimpleTableInfo> listRealTables(String databaseCode);

    void syncTable(String databaseCode, String tableId);

    void syncDb(String databaseCode);
}
