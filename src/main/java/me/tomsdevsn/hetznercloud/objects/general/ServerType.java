package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServerType {

    public Integer id;
    public String name;
    public String description;
    public Integer cores;
    public Integer memory;
    public Integer disk;
    public List<Prices> prices;
    @JsonProperty("storage_type")
    public String storageType;

    @Getter
    @Setter
    public static class Prices {
        public String location;
        @JsonProperty("price_hourly")
        public PriceHourly priceHourly;
        @JsonProperty("price_monthly")
        public PriceMonthly priceMonthly;

        @Getter
        @Setter
        public static class PriceHourly {
            public String net;
            public String gross;
        }

        @Getter
        @Setter
        public static class PriceMonthly {
            public String net;
            public String gross;
        }
    }
}
