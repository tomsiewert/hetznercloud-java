package me.tomsdevsn.hetznercloud.objects.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LBTargetLabelSelector {

    private String selector;
}
