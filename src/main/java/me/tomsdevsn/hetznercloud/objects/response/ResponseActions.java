package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import java.util.List;

@Data
public class ResponseActions {

    private List<Action> actions;
}
