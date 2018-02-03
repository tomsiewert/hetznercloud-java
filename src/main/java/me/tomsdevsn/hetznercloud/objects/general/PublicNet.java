package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublicNet {

    private IPv4 ipv4;
    private IPv6 ipv6;
    @JsonProperty("floating_ips")
    private List<Long> floatingIPs;

    @Getter
    @Setter
    public static class IPv4 {
        public String ip;
        public boolean blocked;
        @JsonProperty("dns_ptr")
        public String dnsPTR;
    }

    @Getter
    @Setter
    public static class IPv6 {
        public String ip;
        public boolean blocked;
        @JsonProperty("dns_ptr")
        public List<DNSPTR> dnsPTR;

        @Getter
        @Setter
        public static class DNSPTR {
            public String ip;
            @JsonProperty("dns_ptr")
            public String dnsPTR;
        }
    }
}
