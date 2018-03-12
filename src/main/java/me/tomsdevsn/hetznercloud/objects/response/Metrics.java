package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;

@Data
public class Metrics {

    @JsonDeserialize(using = DateDeserializer.class)
    private Date start;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date end;
    private Long step;
    @JsonProperty("time_series")
    private TimeSeries timeSeries;

    @Data
    public static class TimeSeries {

        @JsonProperty("name_of_timeseries")
        private NameOfTimeSeries nameOfTimeseries;

        @Data
        public static class NameOfTimeSeries {
            private List<List<String>> values;
        }
    }
}
