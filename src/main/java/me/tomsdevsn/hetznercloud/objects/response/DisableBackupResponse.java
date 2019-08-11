package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Action;

@Data
public class DisableBackupResponse {

    private Action action;
}
