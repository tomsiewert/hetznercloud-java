package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;

@Data
public class BackupAction {

    private Long id;
    private String command;
    private String status;
    private Long progress;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date started;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date finished;
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
