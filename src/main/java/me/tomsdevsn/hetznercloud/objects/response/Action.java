package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class Action {

    public Integer id;
    public String command;
    public String status;
    public long progress;
    public ZonedDateTime started;
    public boolean finished;
    public List<Resources> resources;

    @Getter
    @Setter
    public static class Resources {
        public Integer id;
        public String type;
    }

    @Getter
    @Setter
    public static class Error {
        public String code;
        public String message;
    }
}
