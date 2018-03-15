package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Image;

@Data
public class CreateImageResponse {

    private Image image;
    private Action action;
}
