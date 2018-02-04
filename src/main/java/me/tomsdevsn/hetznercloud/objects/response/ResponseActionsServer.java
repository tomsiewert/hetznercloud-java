package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;

import java.util.List;

@Data
public class ResponseActionsServer {

    private List<Action> actions;
    private Error error;
}
