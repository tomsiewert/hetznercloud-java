package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.LoadBalancerType;

import java.util.List;

@Data
public class LoadBalancerTypesResponse {

    @JsonProperty("load_balancer_types")
    private List<LoadBalancerType> loadBalancerTypes;

}
