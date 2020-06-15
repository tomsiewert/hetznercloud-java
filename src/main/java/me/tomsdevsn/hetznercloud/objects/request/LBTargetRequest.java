package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.LBTargetIP;
import me.tomsdevsn.hetznercloud.objects.general.LBTargetLabelSelector;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LBTargetRequest {

    private String type;
    private Long serverId;
    private boolean usePrivateIp;
    @JsonProperty("label_selector")
    private LBTargetLabelSelector labelSelector;
    private LBTargetIP ip;
}