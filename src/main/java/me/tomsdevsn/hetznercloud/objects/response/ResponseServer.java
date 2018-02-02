package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Server;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseServer {

    public Server server;
    public Action action;
    public String rootPassword;
    private Error error;
}
