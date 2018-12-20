package com.centit.support.metadata.controller;

import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.metadata.po.MetaTable;
import com.centit.support.metadata.service.MetaDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "数据库元数据")
@RestController(value = "metadata")
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
    @ApiImplicitParam(name = "databaseCode", value = "数据库ID")
    @GetMapping(value = "/{databaseCode}/tables")
    public List<MetaTable> metaTables(@PathVariable String databaseCode){
        return metaDataService.listMetaTables(databaseCode);
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
    public void syncDb(@PathVariable String databaseCode){

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

}
