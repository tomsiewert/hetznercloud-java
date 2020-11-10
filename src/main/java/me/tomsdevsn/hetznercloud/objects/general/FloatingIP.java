package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class FloatingIP {

    private Long id;
    private String name;
    private String description;
    private String ip;
    private String type;
    private Long server;
    @JsonProperty("dns_ptr")
    private List<DnsPTR> dnsPTR;
    @JsonProperty("home_location")
    private HomeLocation homeLocation;
    private boolean blocked;
    private Protection protection;
    private Map<String, String> labels;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;

    @Data
    public static class DnsPTR {
        private String ip;
        @JsonProperty("dns_ptr")
        private String dnsPTR;
    }

    @Data
    public static class HomeLocation {
        private Long id;
        private String name;
        private String description;
        private String country;
        private String city;
        private Double latitude;
        private Double longitude;
        @JsonProperty("network_zone")
        private String networkZone;
    }
}
