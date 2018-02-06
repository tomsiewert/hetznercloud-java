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
    public Date start;
    @JsonDeserialize(using = DateDeserializer.class)
    public Date end;
    public Long step;
    @JsonProperty("time_series")
    public TimeSeries timeSeries;

    @Data
    public static class TimeSeries {

        @JsonProperty("name_of_timeseries")
        public NameOfTimeSeries nameOfTimeseries;

        @Data
        public static class NameOfTimeSeries {
            public List<List<String>> values;
        }
    }
}
