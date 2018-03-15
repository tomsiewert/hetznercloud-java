package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Location;

import java.util.List;

@Data
public class LocationsResponse {

    private List<Location> locations;
}
