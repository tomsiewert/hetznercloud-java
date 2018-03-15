package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.ServerType;

@Data
public class ServerTypeResponse {

    @JsonProperty("server_type")
    private ServerType serverType;
}
