package com.centit.support.datapacket.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.stat.resource.po.DataResourceColumn;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

@Repository
public class RmdbQueryColumnDao extends BaseDaoImpl<RmdbQueryColumn, Serializable> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
