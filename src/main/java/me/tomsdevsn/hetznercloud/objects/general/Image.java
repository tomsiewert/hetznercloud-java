package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Image {

    public Integer id;
    public String type;
    public String status;
    public String name;
    public String description;
    public Double imageSize;
    public Double diskSize;
    public ZonedDateTime created;
    public CreatedFrom createdFrom;
    public Integer boundTo;
    public String osFlavor;
    public String osVersion;
    public boolean rapidDeploy;

    @Getter
    @Setter
    public static class CreatedFrom {
        public Integer id;
        public String name;
    }

}
