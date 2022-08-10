package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.IPAssigneeType;

@Data
@Builder
@AllArgsConstructor
public class AssignPrimaryIPRequest {

    @JsonProperty("assignee_id")
    private Long assigneeId;
    @JsonProperty("assignee_type")
    private IPAssigneeType assigneeType;
}