package me.tomsdevsn.hetznercloud.objects.pricing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.ServerType;

import java.util.List;

@Getter
@Setter
public class Pricing {

    public String currency;
    @JsonProperty("vat_rate")
    public double vatRate;
    public Pricing.Image image;
    @JsonProperty("floating_ip")
    public Pricing.FloatingIP floatingIP;
    public Pricing.Traffic traffic;
    @JsonProperty("server_backup")
    public Pricing.ServerBackup serverBackup;
    @JsonProperty("server_types")
    public List<ServerType> serverTypes;

    @Getter
    @Setter
    public static class Image {
        @JsonProperty("price_per_gb_month")
        public PricePerGBMonth pricePerGBMonth;

        @Getter
        @Setter
        public static class PricePerGBMonth {
            public double net;
            public double gross;
        }
    }

    @Getter
    @Setter
    public static class FloatingIP {
        @JsonProperty("price_monthly")
        public PriceMonthly priceMonthly;

        @Getter
        @Setter
        public static class PriceMonthly {
            public double net;
            public double gross;
        }
    }

    @Getter
    @Setter
    public static class Traffic {
        @JsonProperty("price_per_tb")
        public PricePerTB pricePerTB;

        @Getter
        @Setter
        public static class PricePerTB {
            public double net;
            public double gross;
        }
    }

    @Getter
    @Setter
    public static class ServerBackup {
        public double percentage;
    }
}
