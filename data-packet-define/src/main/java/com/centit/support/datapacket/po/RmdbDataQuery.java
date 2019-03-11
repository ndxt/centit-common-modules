package com.centit.support.datapacket.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

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

}
