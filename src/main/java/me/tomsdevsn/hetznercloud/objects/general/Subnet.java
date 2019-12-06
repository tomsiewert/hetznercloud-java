package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subnet {

    private String type;
    @JsonProperty("ip_range")
    private String ipRange;
    @JsonProperty("network_zone")
    private String networkZone;
    private String gateway;
}
