package com.centit.support.metadata.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.support.metadata.po.MetaTable;

import java.util.Map;

public class MetaTableDao extends BaseDaoImpl<MetaTable, String> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
