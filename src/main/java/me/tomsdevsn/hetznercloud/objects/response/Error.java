package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Error {

    public String code;
    public String message;
    public Details details;

    @Getter
    @Setter
    public static class Details {

        public static List<Field> fields;

        @Getter
        @Setter
        public static class Field {
            public String name;
            public String[] message;
        }
    }
}
