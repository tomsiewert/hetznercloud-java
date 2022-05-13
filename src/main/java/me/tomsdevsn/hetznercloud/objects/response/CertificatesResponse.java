package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Certificate;
import me.tomsdevsn.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class CertificatesResponse {

    private List<Certificate> certificates;
    private Meta meta;
}
