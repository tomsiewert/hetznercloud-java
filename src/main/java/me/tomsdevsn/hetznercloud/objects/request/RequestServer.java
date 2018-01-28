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
public class RequestServer {

    private String name;

    @JsonProperty("server_type")
    private String serverType;
    @Nullable
    private String datacenter;
    @Nullable
    private String location;
    private String image;

    @JsonProperty("start_after_create")
    private boolean startAfterCreate;
    @JsonProperty("ssh_keys")
    @Nullable
    private List<Long> sshKeys;

    @JsonProperty("user_data")
    @Nullable
    private String userData;
}
