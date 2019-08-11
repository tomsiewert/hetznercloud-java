package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.Map;

@Data
public class Volume {

    private Long id;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    private String name;
    private Long server;
    private Location location;
    private Long size;
    @JsonProperty("linux_device")
    private String linuxDevice;
    private Protect protection;
    private Map<String, String> labels;
    private String status;
    private String format;

    @Data
    public static class Protect {
        private boolean delete;
    }
}
