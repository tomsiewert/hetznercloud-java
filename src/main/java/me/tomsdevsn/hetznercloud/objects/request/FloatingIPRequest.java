package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.tomsdevsn.hetznercloud.objects.enums.IPType;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FloatingIPRequest {

    private IPType type;
    @JsonProperty("home_location")
    private String homeLocation;
    private Long server;
    private String description;
    private String name;
    @Singular
    private Map<String, String> labels;
}