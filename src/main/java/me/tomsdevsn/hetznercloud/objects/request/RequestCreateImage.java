package me.tomsdevsn.hetznercloud.objects.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreateImage {

    public String description;
    public String type;
}
