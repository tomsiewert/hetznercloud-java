package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Server;

@Getter
@Setter
public class ResponseServer {

    private Server server;
    private Action action;
    @JsonProperty("root_password")
    private String rootPassword;
    private Error error;
}
