package com.centit.support.datapacket.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Map;

@ApiModel
@Data
@NoArgsConstructor
@Entity
public class RmdbDataQuery {

    private String queryId;

    private String packetId;

    private String queryName;

    private String queryDesc;

    private String databaseId;

    private String querySQL;

    /**
     * 字段名 描述
     */
    private String fieldNamesJSON;
    /**
     * 详细描述
     */
    private String packetDesc;

    @Transient
    private Map<String, String> fieldNames;

}
