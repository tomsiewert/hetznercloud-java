package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServerType {

    private Long id;
    private String name;
    private String description;
    private Integer cores;
    private Integer memory;
    private Integer disk;
    private List<Prices> prices;
    @JsonProperty("storage_type")
    private String storageType;

    @Getter
    @Setter
    public static class Prices {
        private String location;
        @JsonProperty("price_hourly")
        private PriceHourly priceHourly;
        @JsonProperty("price_monthly")
        private PriceMonthly priceMonthly;

        @Getter
        @Setter
        public static class PriceHourly {
            private String net;
            private String gross;
        }

        @Getter
        @Setter
        public static class PriceMonthly {
            private String net;
            private String gross;
        }
    }
}
