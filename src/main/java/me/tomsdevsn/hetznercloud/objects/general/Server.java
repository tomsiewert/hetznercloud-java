package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;

@Getter
@Setter
public class Server {

    private Long id;
    private String name;
    private String status;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    @JsonProperty("public_net")
    private PublicNet publicNet;
    @JsonProperty("server_type")
    private ServerType serverType;
    private Datacenter datacenter;
    private Image image;
    private ISO iso;
    @JsonProperty("rescue_enabled")
    private boolean rescueEnabled;
    private boolean locked;
    @JsonProperty("backup_window")
    private String backupWindow;
    @JsonProperty("outgoing_traffic")
    private Long outgoingTraffic;
    @JsonProperty("ingoing_traffic")
    private Long ingoingTraffic;
    @JsonProperty("included_traffic")
    private Long includedTraffic;
}
