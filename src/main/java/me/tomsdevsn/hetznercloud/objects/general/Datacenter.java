package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Datacenter {

    private Long id;
    private String command;
    private String description;
    private Location location;
    @JsonProperty("server_types")
    private ServerTypes serverTypes;

    @Getter
    @Setter
    public static class Location {
        private Integer id;
        private String name;
        private String description;
        private String country;
        private String city;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Setter
    public static class ServerTypes {
        private List<Long> supported;
        private List<Long> available;
    }
}
