package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Datacenter;

import java.util.List;

@Getter
@Setter
public class ResponseDatacenters {

    public List<Datacenter> datacenters;
    public long recommendation;
}
