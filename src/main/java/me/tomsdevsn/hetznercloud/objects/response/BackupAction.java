package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import java.util.List;

@Data
public class BackupAction {

    private Long id;
    private String command;
    private String status;
    private Long progress;
    private String started;
    private String finished;
    private List<Resources> resources;
    private Error error;

    @Data
    public static class Resources {
        private Long id;
        private String type;
    }

    @Data
    public static class Error {
        public String code;
        public String message;
    }
}
