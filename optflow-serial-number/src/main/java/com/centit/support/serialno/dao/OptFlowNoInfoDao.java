package com.centit.support.serialno.dao;

import com.centit.support.serialno.po.OptFlowNoInfo;
import com.centit.support.serialno.po.OptFlowNoInfoId;

public interface OptFlowNoInfoDao {

  /**
   * 根据ID查询
   * @param cid 复合主键
   * @return 操作流水号
   */
    OptFlowNoInfo getObjectById(OptFlowNoInfoId cid);

  /**
   * 删除
   * @param cid 复合主键
   */
    void deleteObjectById(OptFlowNoInfoId cid);

  /**
   *
   * @param optFlowNoINfo 操作流水号
   */
  void saveObject(OptFlowNoInfo optFlowNoINfo);

  /**
   * 新增
   * @param optFlowNoInfo 操作流水号
   */
  void saveNewOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo);

  /**
   *  更新
   * @param optFlowNoInfo 操作流水号
   */
  void updateOptFlowNoInfo(OptFlowNoInfo optFlowNoInfo);
}
