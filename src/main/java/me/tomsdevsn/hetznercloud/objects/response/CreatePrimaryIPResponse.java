package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Action;
import me.tomsdevsn.hetznercloud.objects.general.PrimaryIP;

@Data
public class CreatePrimaryIPResponse {

    private Action action;
    @JsonProperty("primary_ip")
    private PrimaryIP primaryIP;
}
