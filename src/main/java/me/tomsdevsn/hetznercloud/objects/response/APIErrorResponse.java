package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.APIError;

@Data
public class APIErrorResponse {

    private APIError error;

}
