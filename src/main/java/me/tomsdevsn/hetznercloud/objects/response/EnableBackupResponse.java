package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Action;

@Data
@Deprecated
public class EnableBackupResponse {

    private Action action;
}
