package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Data;

@Data
public class Location {
    private Long id;
    private String name;
    private String description;
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;
}
