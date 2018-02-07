package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.SSHKey;

import java.util.List;

@Data
public class ResponseSSHKeys {

    @JsonProperty("ssh_keys")
    private List<SSHKey> sshKeys;
}
