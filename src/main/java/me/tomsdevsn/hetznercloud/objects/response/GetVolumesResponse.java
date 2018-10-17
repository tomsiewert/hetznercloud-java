package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Volume;

import java.util.List;

@Data
public class GetVolumesResponse {

    private List<Volume> volumes;
}
