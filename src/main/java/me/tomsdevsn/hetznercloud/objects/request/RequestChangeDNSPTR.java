package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.annotations.Nullable;

@Getter
@Setter
public class RequestChangeDNSPTR {

    public String ip;
    @JsonProperty("dns_ptr")
    @Nullable
    public String dnsPTR;
}
