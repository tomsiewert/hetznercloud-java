package me.tomsdevsn.hetznercloud.objects.general;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;
import me.tomsdevsn.hetznercloud.objects.enums.Architecture;

@Data
public class ISO {

    private Long id;
    private String name;
    private String description;
    private String type;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date deprecated;
    private Meta meta;
    private Architecture architecture;

}
