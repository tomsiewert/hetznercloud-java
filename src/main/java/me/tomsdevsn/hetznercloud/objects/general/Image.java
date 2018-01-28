package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Image {

    public long id;
    public String type;
    public String status;
    public String name;
    public String description;
    @JsonProperty("image_size")
    public Double imageSize;
    @JsonProperty("disk_size")
    public Double diskSize;
    public ZonedDateTime created;
    @JsonProperty("created_from")
    public CreatedFrom createdFrom;
    @JsonProperty("bound_to")
    public Integer boundTo;
    @JsonProperty("os_flavor")
    public String osFlavor;
    @JsonProperty("os_version")
    public String osVersion;
    @JsonProperty("rapid_redeploy")
    public boolean rapidRedeploy;

    @Getter
    @Setter
    public static class CreatedFrom {
        public long id;
        public String name;
    }

}
