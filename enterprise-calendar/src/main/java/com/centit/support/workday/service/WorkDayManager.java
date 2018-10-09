package com.centit.support.workday.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.support.workday.po.WorkDay;

import java.util.Date;

/**
 * @author guo_jh@centit
 *  2018/6/29 11:01
 *  WorkDayManager
 */
public interface WorkDayManager extends BaseEntityManager<WorkDay, Date> {

    boolean isWorkDay(Date workDay);

    int getHolidays(Date beginDate, Date endDate);

    int getWorkDays(Date startDate, Date endDate);
}
