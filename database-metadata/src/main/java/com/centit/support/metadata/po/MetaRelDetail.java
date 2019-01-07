package com.centit.support.metadata.po;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
/**
 * @author zouwy
 */
@Entity
@Table(name = "F_META_REL_DETIAL")
public class MetaRelDetail implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /**
     * 关联代码
     */
    @Column(name = "RELATION_ID")
    private Long relationId;

    /**
     * p字段代码
     */
    @Column(name = "PARENT_COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    private String parentColumnName;

    /**
     * C字段代码
     */
    @Column(name = "CHILD_COLUMN_NAME")
    @NotBlank(message = "字段不能为空")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  childColumnName;

    public String getParentColumnName() {
        return this.parentColumnName;
    }

    public void setParentColumnName(String parentColumnName) {
        this.parentColumnName = parentColumnName;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relation) {
        this.relationId = relation;
    }

    public String getChildColumnName() {
        return this.childColumnName;
    }

    public void setChildColumnName(String childColumnName) {
        this.childColumnName = childColumnName;
    }

    public MetaRelDetail copy(MetaRelDetail other){
  
        this.parentColumnName = other.getParentColumnName();
  
        this.childColumnName= other.getChildColumnName();

        return this;
    }

    public MetaRelDetail copyNotNullProperty(MetaRelDetail other){
  
        if (other.getParentColumnName() != null)
            this.parentColumnName = other.getParentColumnName();
        if (other.getRelationId() != null)
            this.relationId = other.getRelationId();
  
        if( other.getChildColumnName() != null)
            this.childColumnName= other.getChildColumnName();

        return this;
    }

    public MetaRelDetail clearProperties(){
  
        this.childColumnName= null;

        return this;
    }

}
