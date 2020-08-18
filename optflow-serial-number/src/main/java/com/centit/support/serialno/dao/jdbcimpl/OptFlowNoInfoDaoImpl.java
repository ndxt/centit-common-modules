package com.centit.support.serialno.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.support.serialno.dao.OptFlowNoInfoDao;
import com.centit.support.serialno.po.OptFlowNoInfo;
import com.centit.support.serialno.po.OptFlowNoInfoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("optFlowNoInfoDao")
public class OptFlowNoInfoDaoImpl extends BaseDaoImpl<OptFlowNoInfo, OptFlowNoInfoId> implements OptFlowNoInfoDao {

    public static final Logger logger = LoggerFactory.getLogger(OptFlowNoInfoDaoImpl.class);

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("ownerCode", "OWNER_CODE = :ownerCode");
        filterField.put("codeDate", CodeBook.EQUAL_HQL_ID);
        filterField.put("codeCode", "CODE_CODE = :codeCode");
        filterField.put("curNo", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Override
    public OptFlowNoInfo getObjectById(OptFlowNoInfoId cid) {
        return super.getObjectById(cid);
    }

    @Override
    public void deleteObjectById(OptFlowNoInfoId cid) {
        super.deleteObjectById(cid);
    }

    @Override
    public void saveObject(OptFlowNoInfo optFlowNoInfo) {
        super.saveNewObject(optFlowNoInfo);
    }

    @Override
    public void saveNewOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo){
        super.saveNewObject(optFlowNoInfo);
    }

    @Override
    public void updateOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo){
        super.updateObject(optFlowNoInfo);
    }

}
