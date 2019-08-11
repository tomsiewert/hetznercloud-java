package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Singular;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.Map;

@Data
public class Image {

    private Long id;
    private String type;
    private String status;
    private String name;
    private String description;
    @JsonProperty("image_size")
    private Double imageSize;
    @JsonProperty("disk_size")
    private Double diskSize;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    @JsonProperty("created_from")
    private CreatedFrom createdFrom;
    @JsonProperty("bound_to")
    private Long boundTo;
    @JsonProperty("os_flavor")
    private String osFlavor;
    @JsonProperty("os_version")
    private String osVersion;
    @JsonProperty("rapid_redeploy")
    private boolean rapidRedeploy;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date deprecated;
    private Protect protection;
    @Singular
    private Map<String, String> labels;

    @Data
    public static class CreatedFrom {
        private Long id;
        private String name;
    }

    @Data
    public static class Protect {
        private boolean delete;
    }
}
