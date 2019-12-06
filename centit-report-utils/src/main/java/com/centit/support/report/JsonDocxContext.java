package com.centit.support.report;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.support.algorithm.ReflectionOpt;
import fr.opensagres.xdocreport.template.IContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by codefan on 17-9-25.
 * 测试通过 "___" 为Report的保留属性
 */
public class JsonDocxContext implements IContext{
    private JSONObject docObject;

    public JsonDocxContext(){
        docObject = null;
    }

    public JsonDocxContext(Object object){
        if(object instanceof JSONObject) {
            this.docObject = (JSONObject) object;
        }else{
            this.docObject = (JSONObject) JSON.toJSON(object);
        }
    }

    /**
     * Register a Java object with the given key.
     *
     * @param key  主键
     * @param value Java model
     * @return Object 返回旧值
     */
    @Override
    public Object put(String key, Object value) {
        if(docObject==null) {
            docObject = new JSONObject();
        }
        return docObject.put(key, value);
        //throw new IllegalAccessError("Unsupported!");
    }

    /**
     * Get the Java object with the given key which was registered and null otherwise.
     *
     * @param key 关键字，占位符
     * @return 返回结果
     */
    @Override
    public Object get(String key) {
        Object value = null;
        if(docObject!=null) {
            value = ReflectionOpt.attainExpressionValue(docObject, key);
        }
        if(value == null){
            return key.startsWith("___") ? null: new JsonDocxContext();
        }
        if(value instanceof Collection){
            Collection<Object> objects = (Collection<Object>)value;
            ArrayList<JsonDocxContext> valueList = new ArrayList<>(objects.size());
            for(Object obj : objects){
                valueList.add(new JsonDocxContext(obj));
            }
            return valueList;
        } else if(value.getClass().isArray()) {
            int len = Array.getLength(value);
            ArrayList<JsonDocxContext> valueList = new ArrayList<>(len);
            for(int i=0;i<len;i++){
                Object obj = Array.get(value, i);
                valueList.add(new JsonDocxContext(obj));
            }
            return valueList;
        } else {
            return new JsonDocxContext(value);
        }
    }

    /**
     * @param contextMap 内容Map对象
     */
    @Override
    public void putMap(Map<String, Object> contextMap) {
        if(contextMap instanceof JSONObject) {
            this.docObject = (JSONObject) contextMap;
        }else{
            this.docObject = (JSONObject) JSON.toJSON(contextMap);
        }
        //throw new IllegalAccessError("Unsupported!");
    }

    @Override
    public Map<String, Object> getContextMap() {
        return docObject;
    }

    @Override
    public String toString(){
        return docObject==null ? "" : docObject.toJSONString();
    }
}
