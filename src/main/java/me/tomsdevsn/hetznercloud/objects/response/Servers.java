package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Server;

import java.util.List;

@Getter
@Setter
public class Servers {

    private List<Server> servers;
    private Meta meta;
    private Error error;
}
