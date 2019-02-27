package com.centit.support.quartz;

import com.centit.framework.appclient.AppSession;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.support.network.UrlOptUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;

public class HttpRquestJob extends AbstractQuartzJob {
    private AppSession appSession;

    private String requestUrl;
    private Map<String,Object> requestParams;
    private String httpMethod;// get put post delete
    private String requstBody;// jsonString

    @Override
    public boolean runRealJob(JobExecutionContext context) throws JobExecutionException {
        CloseableHttpClient httpClient = null;
        boolean ret = true;
        try {
            httpClient = appSession.allocHttpClient();
            appSession.checkAccessToken(httpClient);
            String retStr;
            switch (httpMethod) {
                case "post":
                    retStr = appSession.jsonPost(httpClient,
                        UrlOptUtils.appendParamsToUrl(requestUrl, requestParams), requstBody);
                break;
                case "put":
                    retStr = appSession.jsonPut(httpClient,
                            UrlOptUtils.appendParamsToUrl(requestUrl, requestParams), requstBody);
                    break;
                case "delete":
                    retStr = appSession.doDelete(httpClient,
                          UrlOptUtils.appendParamsToUrl(requestUrl, requestParams),null);
                    break;
                default: // "get":
                    retStr = appSession.simpleGet(httpClient, requestUrl, requestParams);
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

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setRequstBody(String requstBody) {
        this.requstBody = requstBody;
    }
}
