package com.centit.support.metadata.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.utils.DBType;
import com.centit.support.metadata.utils.FieldType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @author zouwy
*/
@ApiModel
@Data
@Entity
@Table(name = "F_META_COLUMN")
public class MetaColumn implements TableField,java.io.Serializable {
    private static final long serialVersionUID =  1L;

    @ApiModelProperty(value = "表ID")
    @Id
    @Column(name = "TABLE_ID")
    @NotBlank(message = "字段不能为空")
    private Long tableId;

    @ApiModelProperty(value = "字段名")
    @Id
    @Column(name = "COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    private String  columnName;

    @ApiModelProperty(value = "字段显示名")
    @Column(name = "FIELD_LABEL_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  fieldLabelName;

    @ApiModelProperty(value = "字段描述")
    @Column(name = "COLUMN_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String  columnComment;

    @ApiModelProperty(value = "显示顺序")
    @Column(name = "COLUMN_ORDER")
    private Long  columnOrder;

    @ApiModelProperty(value = "字段类型")
    @Column(name = "COLUMN_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  columnFieldType;

    @ApiModelProperty(value = "字段长度")
    @Column(name = "MAX_LENGTH")
    private Integer maxLength;

    @ApiModelProperty(value = "字段精度")
    @Column(name = "SCALE")
    private Integer scale;

    /**
     * 字段类别 null
     */
    @ApiModelProperty(value = "字段类别")
    @Column(name = "ACCESS_TYPE")
    @NotBlank(message = "字段不能为空")
    @Length(  message = "字段长度不能大于{max}")
    private String  accessType;//todo

    /**
     * 是否必填 null
     */
    @ApiModelProperty(value = "是否必填")
    @Column(name = "MANDATORY")
    @Length(  message = "字段长度不能大于{max}")
    private String  mandatory;

    /**
     * 是否为主键 null
     */
    @ApiModelProperty(value = "是否主键")
    @Column(name = "PRIMARYKEY")
    @Length(  message = "字段长度不能大于{max}")
    private String primaryKey;
    /**
     * 状态 null
     */
    @ApiModelProperty(value = "状态")
    @Column(name = "COLUMN_STATE")
    @NotBlank(message = "字段不能为空")
    @Length(  message = "字段长度不能大于{max}")
    private String  columnState;//todo
    /**
     *  0：没有：1： 数据字典(列表)   2： 数据字典(树型)   3：JSON表达式 4：sql语句   5：SQL（树）
       9 :框架内置字典（用户、机构、角色等等）  Y：年份 M：月份   F:文件（column_Type 必须为 varchar（64））
     */

    @Column(name = "REFERENCE_TYPE")
    @Length(  message = "字段长度不能大于{max}")
    private String  referenceType;
    /**
     * 引用数据 根据paramReferenceType类型（1,2,3）填写对应值
     */
    @Column(name = "REFERENCE_DATA")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String  referenceData;
    /**
     * 约束表达式 regex表达式
     */
    @Column(name = "VALIDATE_REGEX")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  validateRegex;
    /**
     * 约束提示 约束不通过提示信息
     */
    @Column(name = "VALIDATE_INFO")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  validateInfo;

    /**
     * 自动生成规则   C 常量  U uuid S sequence
     */
    @Column(name = "AUTO_CREATE_RULE")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  autoCreateRule;

    /**
     * 自动生成参数 常量的值或者sequence的名字
     */
    @Column(name = "AUTO_CREATE_PARAM")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String  autoCreateParam;
    /**
     * 更改时间 null
     */
    @Column(name = "LAST_MODIFY_DATE")
    private Date  lastModifyDate;
    /**
     * 更改人员 null
     */
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String  recorder;

    @Transient
    private DBType databaseType;

    public void setDatabaseType(DBType databaseType) {
        this.databaseType = databaseType;
    }


  
    /**
     * 这个是用于生产数据库表创建语句的，不是用来生成表单默认值的
     */
    public String getDefaultValue() {
        return "C".equals(autoCreateRule)?autoCreateParam:null;
        /*switch(autoCreateRule){
        case "C":
            return autoCreateParam;
        case "U":
            return UuidOpt.getUuidAsString();
        case "S":
            return null;
        default:
            return null;
        }*/
    }


    @Override
    public String getPropertyName() {
        return SimpleTableField.mapPropName(getColumnName());
    }

    public static String mapToFieldType(String columnType,int scale)
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
        return MetaColumn.mapToFieldType(this.columnFieldType,this.scale ==null?0:this.scale);
    }

    @Override
    public boolean isMandatory() {
        return "T".equals(mandatory) ||  "Y".equals(mandatory) || "1".equals(mandatory);
    }

    public boolean isPrimaryKey() {
        return "T".equals(primaryKey) ||  "Y".equals(primaryKey) || "1".equals(primaryKey);
    }

    @Override
    public int getMaxLength() {
        if("string".equalsIgnoreCase(this.columnFieldType) ||
                "integer".equalsIgnoreCase(this.columnFieldType)||
                "float".equalsIgnoreCase(this.columnFieldType) ||
                "varchar".equalsIgnoreCase(this.columnFieldType)||
                "number".equalsIgnoreCase(this.columnFieldType))
            return maxLength ==null?0: maxLength.intValue();
        return 0;
    }
    @Override
    public int getPrecision() {
        return getMaxLength();
    }
    @Override
    public int getScale() {
        if("float".equalsIgnoreCase(this.columnFieldType) ||
                "number".equalsIgnoreCase(this.columnFieldType))
            return scale ==null?0: scale.intValue();
        return 0;
    }

    @Override
    @JSONField(serialize=false)
    public String getColumnType() {
        return FieldType.mapToDBColumnType(this.databaseType, this.columnFieldType);
    }
}
