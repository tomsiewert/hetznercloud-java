package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;

@Data
public class Action {

    public Long id;
    public String command;
    public String status;
    public Long progress;
    @JsonDeserialize(using = DateDeserializer.class)
    public Date started;
    @JsonDeserialize(using = DateDeserializer.class)
    public Date finished;
    public List<Resources> resources;
    private Error error;

    @Data
    public static class Resources {
        public Long id;
        public String type;
    }

    @Data
    public static class Error {
        public String code;
        public String message;
    }
}
