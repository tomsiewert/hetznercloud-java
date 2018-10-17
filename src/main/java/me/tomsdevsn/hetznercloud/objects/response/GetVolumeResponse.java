package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Volume;

@Data
public class GetVolumeResponse {

    private Volume volume;
}
