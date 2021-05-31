package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LBTarget {

    private String type;
    private Long serverId;
    @JsonProperty("health_status")
    private List<LBHealthStatus> healthStatus;
    @JsonProperty("use_private_ip")
    private Boolean usePrivateIp;
    private String labelSelector;

    @JsonProperty("server")
    @SuppressWarnings("unchecked")
    private void serverDeserializer(Map<String, Object> server) {
        this.serverId = Integer.toUnsignedLong((Integer) server.get("id"));
    }

    @JsonProperty("label")
    @SuppressWarnings("unchecked")
    private void labelDeserializer(Map<String, Object> label) {
        this.labelSelector = (String) label.get("selector");
    }

}
