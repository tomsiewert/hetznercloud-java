package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Server {

    private Integer id;
    private String name;
    private String status;
    private ZonedDateTime created;
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
    private long outgoingTraffic;
    @JsonProperty("ingoing_traffic")
    private long ingoingTraffic;
    @JsonProperty("included_traffic")
    private long includedTraffic;
}
