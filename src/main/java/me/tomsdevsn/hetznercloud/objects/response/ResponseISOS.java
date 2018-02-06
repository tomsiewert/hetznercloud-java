package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.ISO;

import java.util.List;

@Data
public class ResponseISOS {

    public List<ISO> isos;
}
