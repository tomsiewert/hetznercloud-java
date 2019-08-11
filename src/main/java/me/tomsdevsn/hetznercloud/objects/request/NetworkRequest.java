package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.tomsdevsn.hetznercloud.objects.general.Route;
import me.tomsdevsn.hetznercloud.objects.general.Subnet;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NetworkRequest {

    private String name;
    @JsonProperty("ip_range")
    private String ipRange;
    private List<Subnet> subnets;
    private List<Route> routes;
    @Singular
    private Map<String, String> labels;
}
