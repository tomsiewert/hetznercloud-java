package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Image;

@Getter
@Setter
public class ResponseCreateImage {

    public Image image;
    public Action action;
    public Error error;
}
