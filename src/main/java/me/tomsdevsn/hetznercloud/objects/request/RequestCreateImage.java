package me.tomsdevsn.hetznercloud.objects.request;

import lombok.Data;

@Data
public class RequestCreateImage {

    public String description;
    public String type;
}
