package me.tomsdevsn.hetznercloud.objects.general;

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
    private PublicNet publicNet;
    private ServerType serverType;
    private DataCenter datacenter;
    private Image image;
    private ISO iso;
    private boolean rescueEnabled;
    private boolean locked;
    private String backupWindow;
    private Long outgoingTraffic;
    private Long ingoingTraffic;
    private Long includedTraffic;
}
