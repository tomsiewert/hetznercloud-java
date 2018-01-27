package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.tomsdevsn.hetznercloud.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RequestEnableRescue {

    public String type;
    @JsonProperty("ssh_keys")
    @NotNull
    public List<Long> sshKeys;
}
