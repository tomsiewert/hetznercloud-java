package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class FloatingIP {

    private Long id;
    private String description;
    private String ip;
    private String type;
    private Long server;
    @JsonProperty("dns_ptr")
    private List<DnsPTR> dnsPTR;
    @JsonProperty("home_location")
    private HomeLocation homeLocation;
    private boolean blocked;

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
        private double latitude;
        private double longitude;
    }
}
