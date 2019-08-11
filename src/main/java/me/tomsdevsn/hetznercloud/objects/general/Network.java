package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.Data;
import lombok.Singular;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Network {

    private Long id;
    private String name;
    @JsonProperty("ip_range")
    private String ipRange;
    private List<Subnet> subnets;
    private List<Route> routes;
    private List<Long> servers;
    private Protection protection;
    @Singular
    private Map<String, String> labels;
    @JsonSerialize(using = DateSerializer.class)
    private Date created;

    @Data
    public static class Protection {
        private boolean delete;
    }
}
