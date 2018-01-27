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
public class RequestServer {

    private String name;

    @JsonProperty("server_type")
    private String serverType;
    @NotNull
    private String datacenter;
    @NotNull
    private String location;
    private String image;

    @JsonProperty("start_after_create")
    @NotNull
    private boolean startAfterCreate;
    @JsonProperty("ssh_keys")
    @NotNull
    private List<Long> sshKeys;

    @JsonProperty("user_data")
    @NotNull
    private String userData;
}
