package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Certificate;

@Data
public class CertificateResponse {

    private Certificate certificate;
}
