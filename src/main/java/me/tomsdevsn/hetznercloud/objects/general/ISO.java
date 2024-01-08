package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.enums.Architecture;

@Data
public class ISO {

    private Long id;
    private String name;
    private String description;
    private String type;
    private Meta meta;
    private Architecture architecture;
    private Deprecation deprecation;

}
