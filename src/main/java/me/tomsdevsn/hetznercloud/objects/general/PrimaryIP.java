package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PrimaryIP {

    private Long id;
    private String ip;
    private String name;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @JsonProperty("assignee_type")
    private IPAssigneeType assigneeType;
    private IPType type;
    @JsonProperty("auto_delete")
    private Boolean autoDelete;
    private Boolean blocked;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    private Datacenter datacenter;
    @JsonProperty("dns_ptr")
    private List<DnsPTR> dnsPtr;
    private Map<String, String> labels;
}
