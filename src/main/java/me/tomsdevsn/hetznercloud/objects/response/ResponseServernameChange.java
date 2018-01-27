package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Server;

import java.util.List;

@Getter
@Setter
public class ResponseServernameChange {

    public List<Server> server;

}