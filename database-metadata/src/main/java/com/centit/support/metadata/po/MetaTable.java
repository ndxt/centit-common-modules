package com.centit.support.metadata.po;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.metadata.TableReference;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import com.centit.support.database.utils.DBType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

/**
 * @author zouwy
 */
@Data
@ApiModel(value = "表元数据")
@Entity
@Table(name = "F_META_TABLE")
public class MetaTable implements TableInfo, java.io.Serializable {
    private static final long serialVersionUID = 201901031000L;
    /**
     * 表ID 表编号
     */
    @Id
    @Column(name = "TABLE_ID")
    @ApiModelProperty(value = "表ID")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String tableId;

    /**
     * 所属数据库ID
     */
    @Column(name = "DATABASE_CODE")
    @ApiModelProperty(value = "数据库ID")
    private String databaseCode;

    /**
     * 类别 表 T table /视图 V view /大字段 C LOB/CLOB  目前只能是表
     */
    @Column(name = "TABLE_TYPE")
    @NotBlank(message = "字段不能为空")
    @Pattern(regexp = "[TVC]")//todo regex
    @Length(max = 1, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "表类别（T-表；V-视图；C-大字段）")
    @DictionaryMap(fieldName = "tableTypeText", value = "TableType")
    private String tableType;

    /**
     * 表代码/表名
     */
    @Column(name = "TABLE_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "表名")
    private String tableName;

    /**
     * 字段名 tableType =  C ，大字段是，这是 表结构不会根据字段的变更而变更
     */
    @Column(name = "EXT_COLUMN_NAME")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "未知")
    private String extColumnName;

    /**
     * 字段格式 J: JSON or X: XML
     */
    @Column(name = "EXT_COLUMN_FORMAT")
    @Length(max = 10, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "未知")
    private String extColumnFormat;

    /**
     * 表中文名称
     */
    @Column(name = "TABLE_LABEL_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 100, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "表中文名")
    private String tableLabelName;

    /**
     * 控制表是否可以被修改，不是控制数据
     * 状态 系统 S / R 查询(只读)/ N 新建(读写)
     */
    @Column(name = "TABLE_STATE")
    @NotBlank(message = "字段不能为空")
    @Pattern(regexp = "[SRN]")
    @Length(max = 1, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "表状态（S-系统；R-只读；N-可读写）")
    @DictionaryMap(fieldName = "tableStateText", value = "TableState")
    private String tableState;
    /**
     * 描述
     */
    @ApiModelProperty(value = "表描述")
    @Column(name = "TABLE_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String tableComment;
    /**
     * 与流程中业务关联关系
     * 0: 不关联工作流 1：和流程业务关联 2： 和流程过程关联
     */
    @Column(name = "WORKFLOW_OPT_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "workFlowOptTypeText", value = "WorkFlowType")
    private String workFlowOptType;

    /**
     * /Y/N 更新时是否校验时间戳 添加 Last_modify_time datetime
     */

    @Column(name = "UPDATE_CHECK_TIMESTAMP")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String updateCheckTimeStamp;

    /**
     * 更改时间
     */
    @Column(name = "LAST_MODIFY_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date lastModifyDate;
    /**
     * 更改人员
     */
    @Column(name = "RECORDER")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String recorder;

    @OneToMany(targetEntity = MetaColumn.class)
    @JoinColumn(name = "TABLE_ID", referencedColumnName = "TABLE_ID")
    private List<MetaColumn> mdColumns;

    @OneToMany(targetEntity = MetaRelation.class)
    @JoinColumn(name = "tableId", referencedColumnName = "parentTableId")
    private List<MetaRelation> mdRelations;

    @Transient
    @ApiModelProperty(hidden = true)
    private DBType databaseType;

    public List<MetaColumn> getMdColumns() {
        if (this.mdColumns == null)
            this.mdColumns = new ArrayList<>();
        return this.mdColumns;
    }

    public void addMdColumn(MetaColumn mdColumn) {
        if (this.mdColumns == null)
            this.mdColumns = new ArrayList<>();
        this.mdColumns.add(mdColumn);
    }

    public void removeMdColumn(MetaColumn mdColumn) {
        if (this.mdColumns == null)
            return;
        this.mdColumns.remove(mdColumn);
    }

    public MetaColumn newMdColumn() {
        MetaColumn res = new MetaColumn();

        res.setTableId(this.getTableId());

        return res;
    }

    public List<MetaRelation> getMdRelations() {
        if (this.mdRelations == null)
            this.mdRelations = new ArrayList<>();
        return this.mdRelations;
    }

    public void setMdRelations(List<MetaRelation> mdRelations) {
        this.mdRelations = mdRelations;
    }

    public void addMdRelation(MetaRelation mdRelation) {
        if (this.mdRelations == null)
            this.mdRelations = new ArrayList<>();
        this.mdRelations.add(mdRelation);
    }

    public void removeMdRelation(MetaRelation mdRelation) {
        if (this.mdRelations == null)
            return;
        this.mdRelations.remove(mdRelation);
    }

    public MetaRelation newMdRelation() {
        MetaRelation res = new MetaRelation();

        res.setChildTableId(this.getTableId());

        return res;
    }

    //将数据库表同步到元数据表
    public MetaTable convertFromDbTable(SimpleTableInfo tableInfo){
        //TODO
        this.tableName = tableInfo.getTableName();
        if(StringUtils.isNotBlank(tableInfo.getTableLabelName())) {
            this.tableLabelName = tableInfo.getTableLabelName();
        }
        if(StringUtils.isNotBlank(tableInfo.getTableComment())){
            this.tableComment = tableInfo.getTableComment();
        }
        this.tableType = tableInfo.getTableType();
        this.tableState = "S";
        this.workFlowOptType = "0";
        return this;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public String getPkName() {
        return "PK_" + this.tableName;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public String getSchema() {
        return null;
    }

    /**
     * @return 默认排序语句
     */
    @Override
    @ApiModelProperty(hidden = true)
    public String getOrderBy() {
        return null;
    }

    @Override
    public MetaColumn findFieldByName(String name) {
        if (mdColumns == null)
            return null;
        for (MetaColumn c : mdColumns) {
            if (c.getPropertyName().equals(name))
                return c;
        }
        return null;
    }

    @Override
    public MetaColumn findFieldByColumn(String name) {
        if (mdColumns == null)
            return null;
        for (MetaColumn c : mdColumns) {
            if (c.getColumnName().equals(name))
                return c;
        }
        return null;
    }

    @Override
    public boolean isParmaryKey(String colName) {
        if (mdColumns == null)
            return false;
        for (MetaColumn c : mdColumns) {
            if (c.getColumnName().equals(colName)) {
                return c.isPrimaryKey();
            }
        }
        return false;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public List<MetaColumn> getColumns() {
        return mdColumns;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public List<String> getPkColumns() {
        if (mdColumns == null)
            return null;

        List<String> pks = new ArrayList<>();
        for (MetaColumn c : mdColumns) {
            if (c.isPrimaryKey()) {
                pks.add(c.getColumnName());
            }
        }
        return pks;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public List<? extends TableReference> getReferences() {
        return null;
    }
}
