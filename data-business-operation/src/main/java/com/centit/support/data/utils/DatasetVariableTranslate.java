package com.centit.support.data.utils;

import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.compiler.VariableTranslate;
import com.centit.support.data.core.DataSet;

public class DatasetVariableTranslate implements VariableTranslate {

    private DataSet dataSet;
    private int offset;
    private int length;
    private int currentPos;
    //public
    public DatasetVariableTranslate(){
        dataSet = null;
    }
    public DatasetVariableTranslate(DataSet dataSet) {
        this.dataSet = dataSet;
    }

     /**
     * 默认返回业务模型对象的属性值 , request 队形的参数
      /**变量名 -》变量值的转变
      *变量 是用 ${变量名}
      *如果这个变量不存在，返回空字符串 "''"
      * @param varName 变量
      * @return 值
      */
    @Override
    public Object getVarValue(String varName) {
        if(dataSet ==null)
            return null;
        int n = varName.lastIndexOf('.');

        if(n>0){
            String fieldName = varName.substring(0,n);
            if(n<varName.length()) {
                String indexStr = varName.substring(n + 1);
                if (indexStr.charAt(0) == '_'){
                    int pos = NumberBaseOpt.castObjectToInteger(indexStr.substring(1),0);
                    if(currentPos + pos < offset + length){
                        return dataSet.getData().get(currentPos + pos).get(fieldName);
                    }else{
                        return null;
                    }
                }else{
                    int pos = NumberBaseOpt.castObjectToInteger(indexStr,0);
                    if(currentPos - pos >= offset){
                        return dataSet.getData().get(currentPos - pos).get(fieldName);
                    }else{
                        return null;
                    }
                }
            }
        }

        return dataSet.getData().get(currentPos).get(varName);
    }

    @Override
    public Object getLabelValue(String labelName) {
        return getVarValue(labelName);
    }

    public void setDataSet(DataSet varObj) {
        this.dataSet = varObj;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }
}

