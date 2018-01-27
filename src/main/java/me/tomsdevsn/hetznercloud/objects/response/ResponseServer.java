package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Server;

@Getter
@Setter
public class ResponseServer {

    public Server server;
    public Action action;
    public String rootPassword;
}
