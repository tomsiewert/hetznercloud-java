package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Meta;
import me.tomsdevsn.hetznercloud.objects.general.Network;

import java.util.List;

@Data
public class NetworksResponse {

    private List<Network> networks;
    private Meta meta;
}
