package com.centit.support.metadata.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.metadata.TableReference;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author zouwy
 */
@ApiModel
@Data
@Entity
@Table(name = "F_META_RELATION")
public class MetaRelation implements TableReference, java.io.Serializable {
    private static final long serialVersionUID =  201901041034L;

    /**
     * 关联代码 关联关系，类似与外键，但不创建外键
     */
    @Id
    @Column(name = "RELATION_ID")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private Long relationId;

    /**
     * 主表表ID 表单主键
     */
    @ApiModelProperty(value = "主表ID")
    @Column(name = "PARENT_TABLE_ID")
    private Long  parentTableId;

    /**
     * 从表表ID 表单主键
     */
    @ApiModelProperty(value = "从表ID")
    @Column(name = "CHILD_TABLE_ID")
    private String  childTableId;

    /**
     * 关联名称
     */
    @ApiModelProperty(value = "关联名称")
    @Column(name = "RELATION_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  relationName;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @Column(name = "RELATION_STATE")
    private String  relationState;

    /**
     * 关联说明
     */
    @ApiModelProperty(value = "描述")
    @Column(name = "RELATION_COMMENT")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String  relationComment;

    /**
     * 更改时间
     */
    @Column(name = "LAST_MODIFY_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.ALWAYS, value = "today()")
    private Date  lastModifyDate;

    /**
     * 更改人员
     */
    @Column(name = "RECORDER")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName = "recorderName", value = "userCode")
    private String  recorder;

    private Set<MetaRelDetail> relationDetails;

    @Override
    public String getReferenceCode() {
        return String.valueOf( this.relationId);
    }
    @Override
    public String getReferenceName() {
        return this.relationName;
    }
    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return String.valueOf( this.childTableId);
    }
    @Override
    public String getParentTableName() {
        // TODO Auto-generated method stub
        return String.valueOf( this.parentTableId);
    }
    @Override
    public Map<String, String> getReferenceColumns() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean containColumn(String sCol) {
        // TODO Auto-generated method stub
        return false;
    }
}
