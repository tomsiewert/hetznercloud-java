package me.tomsdevsn.hetznercloud.objects.general;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.enums.Architecture;
import me.tomsdevsn.hetznercloud.objects.pricing.LocationPrice;

@Data
public class ServerType {

    private Long id;
    private String name;
    private String description;
    private Long cores;
    private Long memory;
    private Long disk;
    private Boolean deprecated;
    private List<LocationPrice> prices;
    @JsonProperty("storage_type")
    private String storageType;
    @JsonProperty("cpu_type")
    private String cpuType;
    private Architecture architecture;

}
