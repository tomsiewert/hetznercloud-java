package me.tomsdevsn.hetznercloud.objects.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestAttachISO {

    public String iso;
}
