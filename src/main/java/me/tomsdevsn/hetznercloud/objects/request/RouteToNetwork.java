package me.tomsdevsn.hetznercloud.objects.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RouteToNetwork {

    private String destination;
    private String gateway;
}
