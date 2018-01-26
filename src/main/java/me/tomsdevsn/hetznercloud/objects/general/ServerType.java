package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerType {

    public Integer id;
    public String name;
    public String description;
    public Integer cores;
    public Integer memory;
    public Integer disk;

    @Getter
    @Setter
    public static class Prices {
        public String location;
        public PriceHourly priceHourly;
        public PriceMonthly priceMonthly;
        public String storageType;

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
