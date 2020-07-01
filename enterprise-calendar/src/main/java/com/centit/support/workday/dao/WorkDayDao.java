package com.centit.support.workday.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.support.workday.po.WorkDay;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : guo_jh
 * Date: 2018/6/29 10:59
 * Description:
 */
@Repository
public class WorkDayDao extends BaseDaoImpl<WorkDay, Date> {
    public static final Log log = LogFactory.getLog(WorkDayDao.class);

    public WorkDayDao() {
    }

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap();
        filterField.put("workDay", "EQUAL");
        filterField.put("dayType", "EQUAL");
        filterField.put("workTimeType", "EQUAL");
        filterField.put("workDayDesc", "EQUAL");
        filterField.put("startDate", "to_char(workDay,'yyyy-MM-dd') >= to_char(:startDate,'yyyy-MM-dd')");
        filterField.put("endDate", "to_char(workDay,'yyyy-MM-dd') <= to_char(:endDate,'yyyy-MM-dd')");
        return filterField;
    }
}
