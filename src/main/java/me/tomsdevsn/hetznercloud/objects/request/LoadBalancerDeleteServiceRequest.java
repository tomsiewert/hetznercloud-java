package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.LBService;
import me.tomsdevsn.hetznercloud.objects.general.LBTarget;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadBalancerDeleteServiceRequest {

    @JsonProperty("listen_port")
    private long listenPort;
  }
