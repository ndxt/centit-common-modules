package com.centit.support.metadata.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import com.centit.support.database.utils.DBType;
import com.centit.support.metadata.utils.FieldType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author zouwy
*/
@ApiModel
@Data
@NoArgsConstructor
@Entity
@Table(name = "F_MD_COLUMN")
public class MetaColumn implements TableField,java.io.Serializable {
    private static final long serialVersionUID =  201901071109L;

    @ApiModelProperty(value = "表ID", hidden = true)
    @Id
    @Column(name = "TABLE_ID")
    @NotBlank(message = "字段不能为空")
    private String tableId;

    @ApiModelProperty(value = "字段名", hidden = true)
    @Id
    @Column(name = "COLUMN_CODE")
    @NotBlank(message = "字段不能为空")
    private String  columnCode;

    @ApiModelProperty(value = "字段显示名（可编辑）")
    @Column(name = "COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String columnName;

    @ApiModelProperty(value = "字段描述（可编辑）")
    @Column(name = "COLUMN_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String  columnComment;

    @ApiModelProperty(value = "显示顺序（可编辑）")
    @Column(name = "COLUMN_ORDER")
    private Long columnOrder;

    @ApiModelProperty(value = "字段类型", hidden = true)
    @Column(name = "COLUMN_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String columnType;

    @ApiModelProperty(value = "字段长度", hidden = true)
    @Column(name = "COLUMN_LENGTH")
    private int columnLength;

    @ApiModelProperty(value = "字段精度", hidden = true)
    @Column(name = "COLUMN_PRECISION")
    private int columnPrecision;

    /**
     * 字段类别 控制自定义表单中是否可以 访问字段
     * 比如： 最后修改时间、工作流相关的字段、更新版本号，自定义表单中就不用显示
     */
    @ApiModelProperty(value = "字段类别（可编辑）(字段类别.H：隐藏；R：只读；C：只能创建不能修改；N：可读写)")
    @Column(name = "ACCESS_TYPE")
    @NotBlank(message = "字段不能为空")
    @Pattern(regexp = "[HRCN]")
    @Length(  message = "字段长度不能大于{max}")
    private String  accessType;

    /**
     * 是否必填 T-F
     */
    @ApiModelProperty(value = "是否必填", hidden = true)
    @Column(name = "MANDATORY")
    @Pattern(regexp = "[TF]")
    @Length(  message = "字段长度不能大于{max}")
    private String  mandatory;

    /**
     * 是否为主键
     */
    @ApiModelProperty(value = "是否主键", hidden = true)
    @Column(name = "PRIMARY_KEY")
    @Pattern(regexp = "[TF]")
    @Length(  message = "字段长度不能大于{max}")
    private String primaryKey;
    /**
     * 状态 控制列是否可以被修改 系统 S / R 查询(只读)/ N 新建(读写)
     */
    @ApiModelProperty(value = "状态 系统 S / R 查询(只读)/ N 新建(读写)")
    @Column(name = "COLUMN_STATE")
    @NotBlank(message = "字段不能为空")
    @Length(  message = "字段长度不能大于{max}")
    private String  columnState;

    /**
     * 约束关系 专指程序约束类别
     */
    @Column(name = "CONSTRAINT_DESC")
    private String constraintDesc;

    /**
     * 参考字典
     */
    @ApiModelProperty(value = "参考字典")
    @Column(name = "REF_DATA_CATALOG")
    private String refDataCatalog;

    /**
     * 参考SQL语句
     */
    @ApiModelProperty(value = "参考SQL")
    @Column(name = "REF_DATA_SQL")
    private String refDataSql;

    /**
     * 更改时间
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "RECORD_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE, condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date  recordDate;
    /**
     * 更改人员 null
     */
    @ApiModelProperty(value = "更改人员", hidden = true)
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String  recorder;

    @Transient
    @ApiModelProperty(hidden = true)
    private DBType databaseType;

    public MetaColumn(@NotBlank(message = "字段不能为空") String tableId, @NotBlank(message = "字段不能为空") String columnName) {
        this.tableId = tableId;
        this.columnCode = columnName;
    }

    public MetaColumn convertFromTableField(SimpleTableField tableField){
        this.columnCode = tableField.getColumnName();
        this.columnType = tableField.getColumnType();
        if(StringUtils.isNotBlank(tableField.getFieldLabelName())){
            this.columnName = tableField.getFieldLabelName();
        }
        if(StringUtils.isNotBlank(tableField.getColumnComment()) && StringUtils.isBlank(this.columnComment)){
            this.columnComment = tableField.getColumnComment();
        }
        this.columnLength = tableField.getMaxLength();
        this.columnPrecision = tableField.getScale();
        this.mandatory = tableField.isMandatory() ? "T" : "F";
        this.accessType =  "N";
        this.columnState = "S";
        return this;
    }

    /**
     * 这个是用于生产数据库表创建语句的，不是用来生成表单默认值的
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    @Override
    public String getDefaultValue() {
        return null;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public String getPropertyName() {
        return SimpleTableField.mapPropName(getColumnCode());
    }

    private static String mapToFieldType(String columnType,int scale)
    {
        if("NUMBER".equalsIgnoreCase(columnType) ||
           "INTEGER".equalsIgnoreCase(columnType)||
           "DECIMAL".equalsIgnoreCase(columnType) ){
            if( scale > 0 )
                return FieldType.FLOAT;
            else
                return FieldType.INTEGER;
        }else if("FLOAT".equalsIgnoreCase(columnType)){
            return FieldType.FLOAT;
        }else if("CHAR".equalsIgnoreCase(columnType) ||
               "VARCHAR".equalsIgnoreCase(columnType)||
               "VARCHAR2".equalsIgnoreCase(columnType)||
               "STRING".equalsIgnoreCase(columnType) ){
            return FieldType.STRING;
        }else if("DATE".equalsIgnoreCase(columnType) ||
                   "TIME".equalsIgnoreCase(columnType) ){
            return FieldType.DATE;
        }else if("TIMESTAMP".equalsIgnoreCase(columnType)||
                   "DATETIME".equalsIgnoreCase(columnType) ){
            return FieldType.DATETIME;
        }else if("CLOB".equalsIgnoreCase(columnType) /*||
                   "LOB".equalsIgnoreCase(sDBType)||
                   "BLOB".equalsIgnoreCase(sDBType)*/ ){
            return FieldType.TEXT;
        }else if("BOOLEAN".equalsIgnoreCase(columnType) ){
            return FieldType.BOOLEAN;
        }else
            return columnType;
    }

    @Override
    public String getJavaType() {
        return MetaColumn.mapToFieldType(this.columnType,this.columnPrecision);
    }

    @Override
    public boolean isMandatory() {
        return "T".equals(mandatory);
    }

    public boolean isPrimaryKey() {
        return "T".equals(primaryKey);
    }

    public int getColumnLength() {
        if("string".equalsIgnoreCase(this.columnType) ||
                "integer".equalsIgnoreCase(this.columnType)||
                "float".equalsIgnoreCase(this.columnType) ||
                "varchar".equalsIgnoreCase(this.columnType)||
                "number".equalsIgnoreCase(this.columnType))
            return columnLength;
        return 0;
    }

    public void setColumnLength(int columnLength){
        this.columnLength = columnLength;
    }


    @Override
    public int getPrecision() {
        return getColumnLength();
    }

    public int getColumnPrecision() {
        if("float".equalsIgnoreCase(this.columnType) ||
                "number".equalsIgnoreCase(this.columnType))
            return columnPrecision;
        return 0;
    }


    @Override
    public String getColumnType() {
        return FieldType.mapToDBColumnType(this.databaseType, this.columnType);
    }

    @Override
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    public String getFieldLabelName() {
        return this.columnName;
    }

    @Override
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    public int getMaxLength() {
        return this.getColumnLength();
    }

    @Override
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    public int getScale() {
        return this.getColumnPrecision();
    }
}
