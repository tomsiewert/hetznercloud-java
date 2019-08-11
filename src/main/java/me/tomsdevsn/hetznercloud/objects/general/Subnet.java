package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Subnet {

    private String type;
    @JsonProperty("ip_range")
    private String ipRange;
    @JsonProperty("neetwork_zone")
    private String networkZone;
    private String gateway;
}
