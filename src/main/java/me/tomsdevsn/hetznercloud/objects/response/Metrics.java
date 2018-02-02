package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Metrics {

    public String start;
    public String end;
    public long step;
    @JsonProperty("time_series")
    public TimeSeries timeSeries;

    @Getter
    @Setter
    public static class TimeSeries {

        @JsonProperty("name_of_timeseries")
        public NameOfTimeSeries nameOfTimeseries;

        @Getter
        @Setter
        public static class NameOfTimeSeries {
            public List<String> values;
        }
    }
}
