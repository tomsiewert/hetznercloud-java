package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseActionServer {

    private long id;
    @JsonProperty("action_id")
    private long actionID;
}
