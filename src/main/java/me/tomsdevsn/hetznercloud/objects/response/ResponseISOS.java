package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.ISO;

import java.util.List;

@Getter
@Setter
public class ResponseISOS {

    public List<ISO> isos;
    public Meta meta;
    public Error error;
}
