package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Datacenter;

@Data
public class DatacenterResponse {

    private Datacenter datacenter;
}
