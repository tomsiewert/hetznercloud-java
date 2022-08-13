package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Action;
import me.tomsdevsn.hetznercloud.objects.general.Firewall;

import java.util.List;

@Data
public class CreateFirewallResponse {

    private List<Action> actions;
    private Firewall firewall;

}
