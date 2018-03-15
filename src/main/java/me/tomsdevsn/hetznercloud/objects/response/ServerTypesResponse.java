package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.ServerType;

import java.util.List;

@Data
public class ServerTypesResponse {

    @JsonProperty("server_types")
    private List<ServerType> serverTypes;
}
