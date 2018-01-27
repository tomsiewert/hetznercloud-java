package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEnableRescue {

    @JsonProperty("root_password")
    public String rootPassword;
    public Action action;
    public Error error;
}
