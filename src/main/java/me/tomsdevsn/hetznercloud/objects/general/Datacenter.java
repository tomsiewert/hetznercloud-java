package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Datacenter {

    public long id;
    public String command;
    public String description;
    public Location location;
    @JsonProperty("server_types")
    public ServerTypes serverTypes;

    @Getter
    @Setter
    public static class Location {
        public Integer id;
        public String name;
        public String description;
        public String country;
        public String city;
        public Double latitude;
        public Double longitude;
    }

    @Getter
    @Setter
    public static class ServerTypes {
        public List<Long> supported;
        public List<Long> available;
    }
}
