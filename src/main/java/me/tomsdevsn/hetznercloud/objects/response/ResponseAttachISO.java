package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAttachISO {

    public Action action;
    public Error error;
}
