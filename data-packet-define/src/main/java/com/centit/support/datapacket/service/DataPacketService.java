package com.centit.support.datapacket.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.stat.resource.po.DataResource;
import com.centit.stat.resource.po.DataResourceColumn;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataPacketService {

    /**
     * 新增数据包
     */
    void createDataResource(DataResource dataResource);

    void updateDataResource(DataResource dataResource);

    void deleteDataResource(String resourceId);

    List<DataResource> listDataResource(Map<String, Object> params, PageDesc pageDesc);

    DataResource getDataResource(String resourceId);

    List<DataResourceColumn> generateColumn(String databaseCode, String sql);

    JSONArray queryData(String databaseCode, String sql, Map<String, Object> params);

    Set<String> generateParam(String sql);

    void updateResourceColumn(DataResourceColumn column);
}
