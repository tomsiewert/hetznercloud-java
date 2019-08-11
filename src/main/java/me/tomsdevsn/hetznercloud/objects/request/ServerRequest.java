package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerRequest {

    private String name;

    @JsonProperty("server_type")
    private String serverType;
    private String datacenter;
    private String location;
    private String image;

    @JsonProperty("start_after_create")
    private boolean startAfterCreate;

    @Singular
    private Map<String, String> labels;

    /**
     * The objects in the list have to be a Long or a String, or it will throw an Exception {@link me.tomsdevsn.hetznercloud.exception.InvalidParametersException}
     */
    @JsonProperty("ssh_keys")
    @Singular
    private List<Object> sshKeys;

    @JsonProperty("user_data")
    private String userData;

    @Singular
    private List<Long> volumes;
    private boolean automount;

    @Singular
    private List<Long> networks;
}
