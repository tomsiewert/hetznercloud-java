package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.SSHKey;

@Data
public class SSHKeyResponse {

    @JsonProperty("ssh_key")
    private SSHKey sshKey;
}
