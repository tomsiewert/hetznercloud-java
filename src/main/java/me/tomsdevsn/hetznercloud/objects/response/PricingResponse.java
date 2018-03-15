package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.pricing.Pricing;

@Data
public class PricingResponse {

    @JsonProperty("pricing")
    private Pricing pricing;
}
