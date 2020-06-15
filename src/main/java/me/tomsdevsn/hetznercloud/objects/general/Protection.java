package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Data;

@Data
public class Protection {

    private Boolean delete;
    private Boolean rebuild;
}
