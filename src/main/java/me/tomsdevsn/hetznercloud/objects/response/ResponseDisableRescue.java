package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDisableRescue {

    public Action action;
    public Error error;
}
