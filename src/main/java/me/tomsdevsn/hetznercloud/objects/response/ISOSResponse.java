package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.ISO;
import me.tomsdevsn.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class ISOSResponse {

    private List<ISO> isos;
    private Meta meta;
}
