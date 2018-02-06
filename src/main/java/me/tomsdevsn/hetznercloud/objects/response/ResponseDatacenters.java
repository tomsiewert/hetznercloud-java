package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Datacenter;

import java.util.List;

@Data
public class ResponseDatacenters {

    public List<Datacenter> datacenters;
    public Long recommendation;
}
