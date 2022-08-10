package me.tomsdevsn.hetznercloud.objects.pricing;

import lombok.Data;

@Data
class Price {

    private Double net;
    private Double gross;
}