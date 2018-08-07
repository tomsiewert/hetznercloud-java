package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ServerType {

    private Long id;
    private String name;
    private String description;
    private Long cores;
    private Long memory;
    private Long disk;
    private List<Prices> prices;
    @JsonProperty("storage_type")
    private String storageType;
    @JsonProperty("cpu_type")
    private String cpuType;

    @Data
    public static class Prices {
        private String location;
        @JsonProperty("price_hourly")
        private PriceHourly priceHourly;
        @JsonProperty("price_monthly")
        private PriceMonthly priceMonthly;

        @Data
        public static class PriceHourly {
            private String net;
            private String gross;
        }

        @Data
        public static class PriceMonthly {
            private String net;
            private String gross;
        }
    }
}
