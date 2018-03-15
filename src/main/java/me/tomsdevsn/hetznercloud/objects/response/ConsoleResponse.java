package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConsoleResponse {

    @JsonProperty("wss_url")
    private String wssURL;
    private String password;
    private Action action;
}
