package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Meta;
import me.tomsdevsn.hetznercloud.objects.general.PlacementGroup;

import java.util.List;

@Data
public class PlacementGroupsResponse {

    @JsonProperty("placement_groups")
    private List<PlacementGroup> placementGroups;
    private Meta meta;
}
