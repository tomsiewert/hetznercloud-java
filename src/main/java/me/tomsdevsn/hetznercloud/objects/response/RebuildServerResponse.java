package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Action;

@Data
public class RebuildServerResponse {

    private Action action;
    @JsonProperty("root_password")
    private String rootPassword;
}
