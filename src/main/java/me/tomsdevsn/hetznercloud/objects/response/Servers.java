package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Meta;
import me.tomsdevsn.hetznercloud.objects.general.Server;

import java.util.List;

@Data
public class Servers {

    private List<Server> servers;
    private Meta meta;
}
