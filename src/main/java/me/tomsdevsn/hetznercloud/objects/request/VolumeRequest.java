package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolumeRequest {

    private Long size;
    private String name;
    private String location;
    private boolean automount;
    private String format;
    private Long server;
    private Map<String, String> labels = new HashMap<>();
}
