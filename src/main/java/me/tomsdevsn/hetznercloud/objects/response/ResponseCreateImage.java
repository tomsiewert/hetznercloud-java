package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Image;

@Data
public class ResponseCreateImage {

    public Image image;
    public Action action;
}
