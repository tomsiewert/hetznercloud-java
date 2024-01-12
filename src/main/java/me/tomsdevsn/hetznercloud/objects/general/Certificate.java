package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import me.tomsdevsn.hetznercloud.deserialize.DateDeserializer;
import me.tomsdevsn.hetznercloud.objects.enums.CertificateType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Certificate {

    private Long id;
    private String name;
    private Map<String, String> labels;
    private String certificate;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("not_valid_before")
    private Date notValidBefore;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("not_valid_after")
    private Date notValidAfter;
    @JsonProperty("domain_names")
    private List<String> domainNames;
    private String fingerprint;
    @JsonProperty("used_by")
    private List<CertificateUsers> usedBy;
    private CertificateType type;

    @Data
    public static class CertificateUsers {
        private Long id;
        private String type;
    }
}
