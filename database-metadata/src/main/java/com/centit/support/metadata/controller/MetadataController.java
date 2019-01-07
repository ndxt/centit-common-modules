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

/*    @ApiOperation(value = "同步数据库表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "databaseCode", value = "数据库ID"),
        @ApiImplicitParam(name = "tableId", value = "表名")
    })
    @GetMapping(value = "/{databaseCode}/{tableId}/synchronization")
    public void syncTable(@PathVariable String databaseCode, @PathVariable String tableId){
        metaDataService.syncTable(databaseCode, tableId);
    }*/

    @ApiOperation(value = "查询单个表元数据")
    @ApiImplicitParam(name = "tableId", value = "表ID")
    @GetMapping(value = "/table/{tableId}")
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public MetaTable getMetaTable(@PathVariable String tableId){
        return metaDataService.getMetaTable(tableId);
    }

    @ApiOperation(value = "查询列元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "tableId", value = "表ID")
    })
    @GetMapping(value = "/{tableId}/columns")
    public PageQueryResult<MetaColumn> listColumns(@PathVariable String tableId, PageDesc pageDesc){
        List<MetaColumn> list = metaDataService.listMetaColumns(tableId, pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个列元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "tableId", value = "表元数据ID"),
        @ApiImplicitParam(name = "columnName", value = "列名")
    })
    @GetMapping(value = "/{tableId}/{columnName}")
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public MetaColumn getColumn(@PathVariable String tableId, @PathVariable String columnName){
        return metaDataService.getMetaColumn(tableId, columnName);
    }

    @ApiOperation(value = "修改表元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "tableId", value = "表ID"),
        @ApiImplicitParam(name = "tableLabelName", value = "中文名"),
        @ApiImplicitParam(name = "tableComment", value = "描述")
    })
    @PutMapping(value = "/table/{tableId}")
    @WrapUpResponseBody
    public void updateMetaTable(@PathVariable String tableId, String tableLabelName, String tableComment){
        metaDataService.updateMetaTable(tableId, tableLabelName, tableComment);
    }

    @ApiOperation(value = "修改列元数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "tableId", value = "表ID"),
        @ApiImplicitParam(name = "columnName", value = "列名")
    })
    @PutMapping(value = "/column")
    public void updateMetaColumns(MetaColumn metaColumn){

    }

    @ApiOperation(value = "查询关联关系元数据")
    @GetMapping(value = "/relation")
    @WrapUpResponseBody
    public PageQueryResult<MetaRelation> metaRelation(PageDesc pageDesc){
        List<MetaRelation> list = metaDataService.listMetaRelation(pageDesc);
        return PageQueryResult.createResultMapDict(list, pageDesc);
    }

    @ApiOperation(value = "新建关联关系元数据")
    @PostMapping(value = "/relation")
    public void createRelation(MetaRelation relation){
        metaDataService.createRelation(relation);
    }

    @ApiOperation(value = "新建关联关系元数据")
    @PutMapping(value = "/relation")
    public void updateRelation(MetaRelation relation){
        metaDataService.createRelation(relation);
    }

}
