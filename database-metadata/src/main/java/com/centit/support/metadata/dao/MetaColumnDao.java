package com.centit.support.metadata.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.support.metadata.po.MetaColumn;

import java.io.Serializable;
import java.util.Map;

public class MetaColumnDao extends BaseDaoImpl<MetaColumn, Serializable> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
