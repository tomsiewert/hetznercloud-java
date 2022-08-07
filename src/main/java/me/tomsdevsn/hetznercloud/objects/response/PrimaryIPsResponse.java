package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Meta;
import me.tomsdevsn.hetznercloud.objects.general.PrimaryIP;

import java.util.List;

@Data
public class PrimaryIPsResponse {

    @JsonProperty("primary_ips")
    private List<PrimaryIP> primaryIPs;
    private Meta meta;
}
