package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseRebuildServer {

    public Action action;
    @JsonProperty("root_password")
    public String rootPassword;
}
