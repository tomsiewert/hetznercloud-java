package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.pricing.Pricing;

@Getter
@Setter
public class ResponsePricing {

    @JsonProperty("pricing")
    public Pricing pricing;
    public Error error;
}
