package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

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

    /**
     * The objects in the list have to be a Long or a String, or it will throw an Exception {@link me.tomsdevsn.hetznercloud.exception.InvalidParametersException}
     */
    @JsonProperty("ssh_keys")
    private List<Object> sshKeys;

    @JsonProperty("user_data")
    private String userData;
}
