package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Certificate;

import java.util.List;

@Data
public class CertificatesResponse {

    private List<Certificate> certificates;
}
