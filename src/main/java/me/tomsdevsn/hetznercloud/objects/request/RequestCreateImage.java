package me.tomsdevsn.hetznercloud.objects.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateImage {

    public String description;
    public String type;
}
