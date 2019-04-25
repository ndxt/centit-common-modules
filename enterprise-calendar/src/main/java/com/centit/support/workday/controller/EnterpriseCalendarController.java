package com.centit.support.workday.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.workday.po.WorkDay;
import com.centit.support.workday.service.WorkDayManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : guo_jh
 * Date: 2018/6/27 19:31
 * Description:企业工作日历
 */

@Controller
@RequestMapping("/enterpriseCalendar")
public class EnterpriseCalendarController extends BaseController {

    @Resource
    private WorkDayManager workDayMag;


    /**
     * 获取指定范围内特殊日期集合
     *
     * @param curDate  当前选中时间,默认取系统当前时间
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/findMarkDay", method = RequestMethod.GET)
    public void findMarkDay(@Valid Date curDate, HttpServletResponse response) {
        curDate = curDate == null ? new Date() : curDate;
        Date startDate = DatetimeOpt.truncateToMonth(curDate);
        Date endDate = DatetimeOpt.seekEndOfMonth(curDate);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("startDate", startDate);
        paramsMap.put("endDate", endDate);
        List<WorkDay> workDays = this.workDayMag.listObjects(paramsMap);
        Map<String, String> markMap = new HashMap<String, String>();
        if (null != workDays && workDays.size() > 0) {
            for (WorkDay workDay : workDays) {
                markMap.put(DatetimeOpt.convertDateToString(workDay.getWorkDay()), CodeRepositoryUtil.getValue("DAY_TYPE", workDay.getDayType()));
            }
        }
        JsonResultUtils.writeSingleDataJson(markMap, response);
    }

    /**
     * 更新日期标记
     *
     * @param workDay 工作日信息
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    public void saveData(WorkDay workDay, HttpServletResponse response) {
        if ("0".equals(workDay.getDayType())) {//还原日期默认标记
            this.workDayMag.deleteObjectById(workDay.getWorkDay());
        } else {//新增或更新日期标记
            WorkDay dbWorkDay = this.workDayMag.getObjectById(workDay.getWorkDay());
            if (dbWorkDay != null) {
                dbWorkDay.copyNotNullProperty(workDay);
                this.workDayMag.mergeObject(workDay);
            } else {
                this.workDayMag.saveNewObject(workDay);
            }
        }
        JsonResultUtils.writeSingleDataJson(DatetimeOpt.convertDateToString(workDay.getWorkDay()), response);
    }

    @RequestMapping(value = "/getCurrData", method = RequestMethod.GET)
    public void getCurrData(@Valid Date curDate, HttpServletResponse response,HttpServletRequest request) {
        curDate = curDate == null ? new Date() : curDate;
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("startDate", curDate);
        paramsMap.put("endDate", curDate);
        List<WorkDay> workDays = this.workDayMag.listObjects(paramsMap);
        WorkDay day = new WorkDay();
        if(workDays!=null && workDays.size()>0){
            day = workDays.get(0);
        }else{
            day.setWorkDay(curDate);
        }
        request.setAttribute("day",day);
        try {
            request.getRequestDispatcher("/modules/enterprisecalendar/enterprisecalendar-edit.jsp").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonResultUtils.writeSingleDataJson(day, response);
    }
}
