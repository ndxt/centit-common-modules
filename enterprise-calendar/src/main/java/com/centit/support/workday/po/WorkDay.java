package com.centit.support.workday.po;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : guo_jh
 * Date: 2018/6/29 10:46
 * Description:
 */
@Entity
@Table(
    name = "F_WORK_DAY"
)
public class WorkDay implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(
        name = "WORK_DAY"
    )
    private Date workDay;
    @Column(
        name = "DAY_TYPE"
    )
    @NotBlank(
        message = "字段不能为空"
    )
    @Length(
        min = 0,
        max = 1,
        message = "字段长度不能小于{min}大于{max}"
    )
    private String dayType;
    @Column(
        name = "WORK_TIME_TYPE"
    )
    @Length(
        min = 0,
        max = 20,
        message = "字段长度不能小于{min}大于{max}"
    )
    private String workTimeType;
    @Column(
        name = "WORK_DAY_DESC"
    )
    @Length(
        min = 0,
        max = 255,
        message = "字段长度不能小于{min}大于{max}"
    )
    private String workDayDesc;

    public WorkDay() {
    }

    public WorkDay(Date workDay, String dayType) {
        this.workDay = workDay;
        this.dayType = dayType;
    }

    public WorkDay(Date workDay, String dayType, String workTimeType, String workDayDesc) {
        this.workDay = workDay;
        this.dayType = dayType;
        this.workTimeType = workTimeType;
        this.workDayDesc = workDayDesc;
    }

    public Date getWorkDay() {
        return this.workDay;
    }

    public void setWorkDay(Date workDay) {
        this.workDay = workDay;
    }

    public String getDayType() {
        return this.dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getWorkTimeType() {
        return this.workTimeType;
    }

    public void setWorkTimeType(String workTimeType) {
        this.workTimeType = workTimeType;
    }

    public String getWorkDayDesc() {
        return this.workDayDesc;
    }

    public void setWorkDayDesc(String workDayDesc) {
        this.workDayDesc = workDayDesc;
    }

    public WorkDay copy(WorkDay other) {
        this.setWorkDay(other.getWorkDay());
        this.dayType = other.getDayType();
        this.workTimeType = other.getWorkTimeType();
        this.workDayDesc = other.getWorkDayDesc();
        return this;
    }

    public WorkDay copyNotNullProperty(WorkDay other) {
        if (other.getWorkDay() != null) {
            this.setWorkDay(other.getWorkDay());
        }

        if (other.getDayType() != null) {
            this.dayType = other.getDayType();
        }

        if (other.getWorkTimeType() != null) {
            this.workTimeType = other.getWorkTimeType();
        }

        if (other.getWorkDayDesc() != null) {
            this.workDayDesc = other.getWorkDayDesc();
        }

        return this;
    }

    public WorkDay clearProperties() {
        this.dayType = null;
        this.workTimeType = null;
        this.workDayDesc = null;
        return this;
    }
}
