package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Server;

import java.util.List;

@Data
public class ServerResponse {

    private Server server;
    private Action action;
    @JsonProperty("next_actions")
    private List<Action> nextActions;
    @JsonProperty("root_password")
    private String rootPassword;
}
