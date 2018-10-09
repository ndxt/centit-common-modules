package com.centit.support.workday.service.impl;

import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.workday.dao.WorkDayDao;
import com.centit.support.workday.po.WorkDay;
import com.centit.support.workday.service.WorkDayManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guo_jh
 * Date: 2018/6/29 11:03
 * Description:
 */
@Service
public class WorkDayManagerImpl extends BaseEntityManagerImpl<WorkDay, Date, WorkDayDao> implements WorkDayManager {

    public static final Log log = LogFactory.getLog(WorkDayManagerImpl.class);

    private WorkDayDao workDayDao;

    @Resource
    @NotNull
    public void setWorkDayDAO(WorkDayDao workDayDao) {
        this.workDayDao = workDayDao;
        setBaseDao(this.workDayDao);
    }

    @Override
    public boolean isWorkDay(Date workDay) {
        boolean result = false;
        WorkDay day = this.workDayDao.getObjectById(workDay);
        if (day == null) {
            if (DatetimeOpt.getDayOfWeek(workDay) > 0 && DatetimeOpt.getDayOfWeek(workDay) < 6) {
                result = true;
            }
        } else /*if (day != null)*/ {
            if ("B".equals(day.getDayType())) {//B:周末调休成工作时间
                result = true;
            } else if ("C".equals(day.getDayType()) && DatetimeOpt.getDayOfWeek(workDay) > 0 && DatetimeOpt.getDayOfWeek(workDay) < 6) {//C: 正常上班
                result = true;
            }
        }
        return result;
    }

    @Override
    public int getHolidays(Date startDate, Date endDate) {
        int holidays = DatetimeOpt.calcWeekendDays(startDate, endDate);//获取指定范围内周末的天数
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("startDate", startDate);
        paramsMap.put("endDate", endDate);
        List<WorkDay> list = this.listObjects(paramsMap);
        if (null != list) {
            for (WorkDay workDay : list) {
                if ("A".equals(workDay.getDayType()) && DatetimeOpt.getDayOfWeek(workDay.getWorkDay()) > 0 && DatetimeOpt.getDayOfWeek(workDay.getWorkDay()) < 6) {
                    holidays++;
                } else if ("B".equals(workDay.getDayType()) && (DatetimeOpt.getDayOfWeek(workDay.getWorkDay()) == 0 || DatetimeOpt.getDayOfWeek(workDay.getWorkDay()) == 6)) {
                    holidays--;
                }
            }
        }
        return holidays;
    }

    @Override
    public int getWorkDays(Date startDate, Date endDate) {
        int spanDays = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24 + 1);
        int holidays = this.getHolidays(startDate, endDate);
        return spanDays - holidays;
    }
}
