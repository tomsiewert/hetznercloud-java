package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;
import me.tomsdevsn.hetznercloud.serialize.MetricsSerializer;

import java.util.Date;

@Data
public class Metrics {

    @JsonDeserialize(using = DateDeserializer.class)
    private Date start;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date end;
    private Long step;
    @JsonProperty("time_series")
    @JsonSerialize(using = MetricsSerializer.class)
    private Object timeSeries;
}