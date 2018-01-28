package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseChangeType {

    public Action action;
    public Error error;
}
