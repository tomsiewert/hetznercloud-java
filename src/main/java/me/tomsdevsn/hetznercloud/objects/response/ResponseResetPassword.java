package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseResetPassword {

    @JsonProperty("root_password")
    public String rootPassword;
    public Action action;
}
