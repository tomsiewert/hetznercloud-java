package me.tomsdevsn.hetznercloud.objects.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestRebuildServer {

    public String image;
}
