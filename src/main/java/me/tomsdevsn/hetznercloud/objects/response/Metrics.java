package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Metrics {

    @JsonDeserialize(using = DateDeserializer.class)
    public Date start;
    @JsonDeserialize(using = DateDeserializer.class)
    public Date end;
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
            public List<List<String>> values;
        }
    }
}
