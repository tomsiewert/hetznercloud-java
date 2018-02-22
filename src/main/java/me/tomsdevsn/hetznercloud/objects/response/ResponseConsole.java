package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseConsole {

    @JsonProperty("wss_url")
    private String wssURL;
    private String password;
    private BackupAction action;
}
