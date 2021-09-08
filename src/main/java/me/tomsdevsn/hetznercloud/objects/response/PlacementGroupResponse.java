package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.PlacementGroup;

@Data
public class PlacementGroupResponse {

    @JsonProperty("placement_group")
    private PlacementGroup placementGroup;

}
