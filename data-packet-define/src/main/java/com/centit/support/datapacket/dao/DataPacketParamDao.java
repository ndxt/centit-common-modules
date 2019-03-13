package com.centit.support.datapacket.dao;

import java.io.Serializable;
import java.util.Map;

public class DataPacketParamDao extends BaseDaoImpl<DataPacketParam, Serializable> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
