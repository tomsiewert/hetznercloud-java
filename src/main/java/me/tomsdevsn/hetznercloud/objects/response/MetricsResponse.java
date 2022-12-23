package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Metrics;

@Data
public class MetricsResponse {

    private Metrics metrics;

}
