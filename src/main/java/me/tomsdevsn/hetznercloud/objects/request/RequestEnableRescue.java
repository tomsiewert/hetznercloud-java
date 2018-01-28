package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.annotations.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RequestEnableRescue {

    public String type;
    @JsonProperty("ssh_keys")
    @Nullable
    public List<Long> sshKeys;
}
