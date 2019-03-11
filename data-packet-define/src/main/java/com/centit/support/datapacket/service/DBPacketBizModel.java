package com.centit.support.datapacket.service;

import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.dataopt.core.BizModel;
import com.centit.support.dataopt.core.DataSet;
import com.centit.support.dataopt.dataset.SQLDataSetReader;
import com.centit.support.datapacket.po.DataPacket;
import com.centit.support.datapacket.po.RmdbDataQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DBPacketBizModel implements BizModel {

    private DataPacket dbPacket;
    private IntegrationEnvironment integrationEnvironment;
    private Map<String, Object> queryParams;

    public DBPacketBizModel(){

    }

    public DBPacketBizModel(DataPacket dbPacket){
        this.dbPacket = dbPacket;
    }
    /**
     * 模型名称
     */
    @Override
    public String getModelName() {
        return this.dbPacket.getPacketName();
    }

    /**
     * 模型的标识， 就是对应的主键
     * 或者对应关系数据库查询的参数（数据源参数）
     */
    @Override
    public Map<String, Object> getModeTag() {
        return this.dbPacket.getPacketParams();
    }


    public static DataSourceDescription mapDataSource(DatabaseInfo dbinfo) {
        DataSourceDescription desc=new DataSourceDescription();
        desc.setConnUrl(dbinfo.getDatabaseUrl());
        desc.setUsername(dbinfo.getUsername());
        desc.setPassword(dbinfo.getClearPassword());
        return desc;
    }
    /**
     * 模型数据
     */
    @Override
    public List<DataSet> getBizData() {
        List<DataSet> dataSets = new ArrayList<>();
        for(RmdbDataQuery rdd : this.dbPacket.getDBQueries()) {
            SQLDataSetReader sqlDSR = new SQLDataSetReader();
            sqlDSR.setDataSource( mapDataSource(
                integrationEnvironment.getDatabaseInfo(rdd.getDatabaseId())));
            sqlDSR.setSqlSen(rdd.getQuerySQL());
            dataSets.add(sqlDSR.load(this.queryParams));
        }
        return dataSets;
    }

    public void setDbPacket(DataPacket dbPacket) {
        this.dbPacket = dbPacket;
    }

    public void setIntegrationEnvironment(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }
}
