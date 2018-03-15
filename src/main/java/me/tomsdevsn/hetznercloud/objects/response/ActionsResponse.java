package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import java.util.List;

@Data
public class ActionsResponse {

    private List<Action> actions;
}
