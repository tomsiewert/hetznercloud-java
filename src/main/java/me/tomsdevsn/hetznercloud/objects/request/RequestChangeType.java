package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestChangeType {

    @JsonProperty("upgrade_disk")
    public boolean upgradeDisk;
    @JsonProperty("server_type")
    public String serverType;
}
