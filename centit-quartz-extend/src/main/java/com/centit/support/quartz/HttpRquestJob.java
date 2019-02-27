package com.centit.support.quartz;

import com.centit.framework.appclient.AppSession;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.network.UrlOptUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRquestJob extends AbstractQuartzJob {

    private static ConcurrentHashMap<String, AppSession> appSessionPoolMap = new ConcurrentHashMap<>(10);
    private static AppSession fetchAppSession(String url){
        String sUrl = StringUtils.isBlank(url)? "blank": url;
        AppSession appSession = appSessionPoolMap.get(sUrl);
        if(appSession == null){
            appSession =  new AppSession(url, false, null, null);
            appSessionPoolMap.put(sUrl, appSession);
        }
        return appSession;
    }
    private AppSession appSession;

    private String requestUrl;
    private Map<String,Object> params;
    private String httpMethod;// get put post delete
    private String requstBody;// jsonString

    @Override
    protected void loadExecutionContext(JobExecutionContext context){
        JobDataMap paramMap = context.getMergedJobDataMap();
        appSession = HttpRquestJob.fetchAppSession(paramMap.getString("osUrl"));
        requestUrl = paramMap.getString("requestUrl");
        httpMethod = paramMap.getString("httpMethod");
        requstBody = paramMap.getString("requstBody");
        Object obj = paramMap.get("params");
        if(obj!=null){
            params =(Map) GeneralAlgorithm.castObjectToType(obj, Map.class);
        }else{
            params = new HashMap<>(1);
        }
    }

    @Override
    protected boolean runRealJob(JobExecutionContext context) throws JobExecutionException {
        CloseableHttpClient httpClient = null;
        boolean ret = true;
        try {
            httpClient = appSession.allocHttpClient();
            appSession.checkAccessToken(httpClient);
            String retStr;
            switch (httpMethod) {
                case "post":
                    retStr = appSession.jsonPost(httpClient,
                        UrlOptUtils.appendParamsToUrl(requestUrl, params), requstBody);
                break;
                case "put":
                    retStr = appSession.jsonPut(httpClient,
                            UrlOptUtils.appendParamsToUrl(requestUrl, params), requstBody);
                    break;
                case "delete":
                    retStr = appSession.doDelete(httpClient,
                          UrlOptUtils.appendParamsToUrl(requestUrl, params),null);
                    break;
                default: // "get":
                    retStr = appSession.simpleGet(httpClient, requestUrl, params);
                    break;
            }
            if(StringUtils.isNotBlank(retStr)){
                HttpReceiveJSON json = HttpReceiveJSON.valueOfJson(retStr);
                ret = json.getCode() == 0;
            }
        } catch (Exception e) {
            ret = false;
            logger.error(e.getLocalizedMessage(),e);
        } finally {
            if (appSession != null && httpClient != null) {
                appSession.releaseHttpClient(httpClient);
            }
        }
        return ret;
    }

    public void setAppSession(AppSession appSession) {
        this.appSession = appSession;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setParams(Map<String, Object> requestParams) {
        this.params = requestParams;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setRequstBody(String requstBody) {
        this.requstBody = requstBody;
    }
}
