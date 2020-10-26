package com.centit.search.service.Impl;

import com.centit.search.service.ESServerConfig;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;


/**
 * Created by codefan on 17-6-27.
 */
public class PooledRestClientFactory implements PooledObjectFactory<RestHighLevelClient> {

    private ESServerConfig config;

    public PooledRestClientFactory(ESServerConfig config){
        this.config = config;
    }

    @Override
    public PooledObject<RestHighLevelClient> makeObject() throws Exception {
        RestClientBuilder clientBuilder = RestClient.builder(config.getHttpHosts());
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> p) throws Exception {
        //p.getObject().close();
        RestHighLevelClient client = p.getObject();
        if( client!= null && client.ping(RequestOptions.DEFAULT)) {
            try {
                client.close();
            }catch (Exception e){
                //ignore
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<RestHighLevelClient> p) {
        RestHighLevelClient client = p.getObject();
        try {
            return client.ping(RequestOptions.DEFAULT);
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        RestHighLevelClient client = p.getObject();
        /*boolean response = */client.ping(RequestOptions.DEFAULT);
    }

    @Override
    public void passivateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        // Auto-generated method stub
    }

    public void setConifg(ESServerConfig conifg) {
        this.config = conifg;
    }
}
