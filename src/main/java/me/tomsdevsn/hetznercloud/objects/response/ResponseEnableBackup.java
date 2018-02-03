package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;

@Data
public class ResponseEnableBackup {

    private BackupAction action;
    private Error error;
}
