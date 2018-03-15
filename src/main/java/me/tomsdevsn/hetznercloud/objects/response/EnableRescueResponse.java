package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnableRescueResponse {

    @JsonProperty("root_password")
    private String rootPassword;
    private Action action;
}
