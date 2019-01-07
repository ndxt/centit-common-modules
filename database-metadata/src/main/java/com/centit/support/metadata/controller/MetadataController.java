package com.centit.support.metadata.controller;

import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.metadata.po.MetaColumn;
import com.centit.support.metadata.po.MetaRelation;
import com.centit.support.metadata.po.MetaTable;
import com.centit.support.metadata.service.MetaDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Api(value = "数据库元数据", tags = "元数据")
@RestController
@RequestMapping(value = "metadata")
public class MetadataController {

    @Autowired
    private MetaDataService metaDataService;

    @ApiOperation(value = "数据库列表")
    @GetMapping(value = "/databases")
    @WrapUpResponseBody
    public List<DatabaseInfo> databases(){
        return metaDataService.listDatabase();
    }

    @ApiOperation(value = "表元数据")
    @ApiImplicitParam(name = "databaseCode", value = "数据库代码")
    @GetMapping(value = "/{databaseCode}/tables")
    @WrapUpResponseBody
    public PageQueryResult<MetaTable> metaTables(@PathVariable String databaseCode, PageDesc pageDesc){
        List<MetaTable> list = metaDataService.listMetaTables(databaseCode, pageDesc);
        return PageQueryResult.createResultMapDict(list==null? Collections.emptyList():list,pageDesc);
    }

    @ApiOperation(value = "数据库表")
    @ApiImplicitParam(name = "databaseCode", value = "数据库ID")
    @GetMapping(value = "/{databaseCode}/db_tables")
    public List<SimpleTableInfo> databaseTables(@PathVariable String databaseCode){
        return metaDataService.listRealTables(databaseCode);
    }

    @ApiOperation(value = "同步数据库")
    @ApiImplicitParam(name = "databaseCode", value = "数据库ID")
    @GetMapping(value = "/{databaseCode}/synchronization")
    @WrapUpResponseBody
    public void syncDb(@PathVariable String databaseCode){
        metaDataService.syncDb(databaseCode);
    }

    @ApiOperation(value = "同步数据库表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "databaseCode", value = "数据库ID"),
        @ApiImplicitParam(name = "tableId", value = "表名")
    })
    @GetMapping(value = "/{databaseCode}/{tableId}/synchronization")
    public void syncTable(@PathVariable String databaseCode, @PathVariable String tableId){
        metaDataService.syncTable(databaseCode, tableId);
    }

    @ApiOperation(value = "查询单个表元数据")
    @ApiImplicitParam(name = "tableName", value = "表名")
    @GetMapping(value = "/{databaseCode}/tables/{tableName}")
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public MetaTable getMetaTable(@PathVariable String databaseCode, @PathVariable String tableName){
        return metaDataService.getMetaTable(databaseCode, tableName);
    }

    @ApiOperation(value = "查询列元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "databaseCode", value = "数据库代码"),
        @ApiImplicitParam(name = "tableName", value = "表名")
    })
    @GetMapping(value = "/{databaseCode}/{tableName}/columns")
    public PageQueryResult<MetaColumn> listColumns(@PathVariable String databaseCode, @PathVariable String tableName,
                                                   PageDesc pageDesc){
        List<MetaColumn> list = metaDataService.listMetaColumns(databaseCode, tableName, pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "修改表元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "tableName", value = "表名"),
        @ApiImplicitParam(name = "tableLabelName", value = "中文名"),
        @ApiImplicitParam(name = "tableComment", value = "描述")
    })
    @PutMapping(value = "/{tableName}")
    @WrapUpResponseBody
    public void updateMetaTable(@PathVariable String tableName, String tableLabelName, String tableComment){
        metaDataService.updateMetaTable(tableName, tableLabelName, tableComment);
    }

    @ApiOperation(value = "修改列元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "tableName", value = "表名"),
        @ApiImplicitParam(name = "columnName", value = "列名")
    })
    @PutMapping(value = "/{tableName}/columns/{columnName}")
    public void updateMetaColumns(@PathVariable String tableName, @PathVariable String columnName,
                                  MetaColumn metaColumn){

    }

    @ApiOperation(value = "查询关联关系元数据")
    @GetMapping(value = "/relation")
    public PageQueryResult<MetaRelation> metaRelation(){
        return PageQueryResult.createResultMapDict(null, null);
    }

    @ApiOperation(value = "新建关联关系元数据")
    @PostMapping(value = "/relations")
    public void createRelation(MetaRelation relation){
        metaDataService.createRelation(relation);
    }

}
