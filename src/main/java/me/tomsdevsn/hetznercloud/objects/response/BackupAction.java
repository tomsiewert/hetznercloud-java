package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;

import java.util.List;

@Data
public class BackupAction {

    private long id;
    private String command;
    private String status;
    private long progress;
    private String started;
    private String finished;
    private List<Resources> resources;

    @Data
    public static class Resources {
        private long id;
        private String type;
    }
}
