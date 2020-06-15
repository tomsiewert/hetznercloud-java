package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.LoadBalancer;

@Data
public class LoadBalancerResponse {

    @JsonProperty("load_balancer")
    private LoadBalancer loadBalancer;
}
