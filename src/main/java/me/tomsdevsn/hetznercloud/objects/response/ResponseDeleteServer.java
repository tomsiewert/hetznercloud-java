package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;

@Data
public class ResponseDeleteServer {

    private Action action;
    private Error error;
}
