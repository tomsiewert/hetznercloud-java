package me.tomsdevsn.hetznercloud.objects.request;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UpdateFirewallRequest {

    private Map<String, String> labels;
    private String name;

}
