package me.tomsdevsn.hetznercloud.objects.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
public class Error {

    public Code code;
    public String message;
    public Details details;

    @Getter
    @AllArgsConstructor
    public enum Code {

        FORBIDDEN("Insufficient permissions for this request"),
        INVALID_INPUT("Error while parsing or processing the input"),
        JSON_ERROR("Invalid JSON input in your request"),
        LOCKED("The item you are trying to access is locked (there is already an action running)"),
        NOT_FOUND("Entity not found"),
        RATE_LIMIT_EXCEEDED("Error when sending too many requests"),
        RESOURCE_LIMIT_EXCEEDED("Error when exceeding the maximum quantity of a resource for an account"),
        RESOURCE_UNAVAILABLE("The requested resource is currently unavailable"),
        SERVICE_ERROR("Error within a service"),
        UNIQUENESS_ERROR("One or more of the objects fields must be unique");

        private String description;
    }

    @Data
    public static class Details {

        public static List<Field> fields;

        @Data
        public static class Field {
            public String name;
            public String[] message;
        }
    }
}
