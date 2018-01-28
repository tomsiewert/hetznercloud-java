package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.annotations.NotNull;

@Getter
@Setter
public class RequestChangeDNSPTR {

    public String ip;
    @JsonProperty("dns_ptr")
    @NotNull
    public String dnsPTR;
}
