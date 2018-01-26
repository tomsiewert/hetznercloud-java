package me.tomsdevsn.hetznercloud.objects.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicNet {

    @Getter
    @Setter
    public static class IPv4 {
        public String ip;
        public boolean blocked;
        public String dnsPTR;
    }

    @Getter
    @Setter
    public static class IPv6 {
        public String ip;
        public boolean blocked;
        public DNSPTR dnsPTR;

        @Getter
        @Setter
        public static class DNSPTR {
            public String ip;
            public String dnsPTR;
        }
    }
}
