package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RebuildServerResponse {

    private Action action;
    @JsonProperty("root_password")
    private String rootPassword;
}
