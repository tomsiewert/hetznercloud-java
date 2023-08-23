package me.tomsdevsn.hetznercloud.objects.general;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

@Data
public class Deprecation {

    @JsonDeserialize(using = DateDeserializer.class)
    private Date announced;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date unavailable_after;
}
