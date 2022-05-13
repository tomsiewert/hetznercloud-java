package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Action;
import me.tomsdevsn.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class ActionsResponse {

    private List<Action> actions;
    private Meta meta;
}
