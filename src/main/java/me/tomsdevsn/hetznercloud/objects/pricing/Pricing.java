package me.tomsdevsn.hetznercloud.objects.pricing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.ServerType;

import java.util.List;

@Data
public class Pricing {

    private String currency;
    @JsonProperty("vat_rate")
    private Double vatRate;
    private Pricing.Image image;
    @JsonProperty("floating_ip")
    private Pricing.FloatingIP floatingIP;
    private Pricing.Traffic traffic;
    @JsonProperty("server_backup")
    private Pricing.ServerBackup serverBackup;
    @JsonProperty("server_types")
    private List<ServerType> serverTypes;

    @Data
    public static class Image {
        @JsonProperty("price_per_gb_month")
        private PricePerGBMonth pricePerGBMonth;

        @Data
        public static class PricePerGBMonth {
            private Double net;
            private Double gross;
        }
    }

    @Data
    public static class FloatingIP {
        @JsonProperty("price_monthly")
        private PriceMonthly priceMonthly;

        @Data
        public static class PriceMonthly {
            private Double net;
            private Double gross;
        }
    }

    @Data
    public static class Traffic {
        @JsonProperty("price_per_tb")
        private PricePerTB pricePerTB;

        @Data
        public static class PricePerTB {
            private Double net;
            private Double gross;
        }
    }

    @Data
    public static class ServerBackup {
        private Double percentage;
    }
}
