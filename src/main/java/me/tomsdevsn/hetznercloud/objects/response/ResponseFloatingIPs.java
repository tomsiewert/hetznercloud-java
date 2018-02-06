package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.FloatingIP;

import java.util.List;

@Data
public class ResponseFloatingIPs {

    @JsonProperty("floating_ips")
    private List<FloatingIP> floatingIPs;
}
