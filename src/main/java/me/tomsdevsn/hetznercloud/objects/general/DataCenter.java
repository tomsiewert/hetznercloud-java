package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataCenter {

    public Integer id;
    public String command;
    public String description;
    public Location location;

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
}
