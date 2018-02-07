package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SSHKey {

    private Long id;
    private String name;
    private String fingerprint;
    @JsonProperty("public_key")
    private String publicKey;
}
