package com.centit.support.datapacket.po;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
@Entity
public class DataPacket {

    private String packetId;

    private String packetName;
    /**
     * 数据包类别，主要有 D database， F file ， P directory 文件夹
     */
    private String packetType;

    /**
     * 数据包参数： 查询参数描述
     */
    private String packetParamsJSON;
    /**
     * 详细描述
     */
    private String packetDesc;

    private Map<String, Object> packetParams;

    public DataPacket(){
        packetType = "D";
    }

    public List<RmdbDataQuery> getDBQueries(){
        if("D".equals(packetType)){

        }
        return null;
    }

    public Map<String, Object> getPacketParams(){
        if(packetParams == null && StringUtils.isNotBlank(packetParamsJSON)){
            packetParams = JSONObject.parseObject(packetParamsJSON);
        }
        return packetParams;
    }

    public void setPacketParamsJSON(String packetParamsJSON) {
        this.packetParamsJSON = packetParamsJSON;
        this.packetParams = null;
    }
}
