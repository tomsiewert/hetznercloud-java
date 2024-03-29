package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.enums.TargetType;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LBTarget {

    private TargetType type;
    @JsonProperty("health_status")
    private List<LBHealthStatus> healthStatus;
    @JsonProperty("use_private_ip")
    private Boolean usePrivateIp;
    @JsonProperty("label_selector")
    private LBTargetLabelSelector labelSelector;
    private LBTargetServer server;
    private LBTargetIP ip;
}
