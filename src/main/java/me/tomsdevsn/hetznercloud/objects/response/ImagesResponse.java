package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Image;
import me.tomsdevsn.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class ImagesResponse {

    private List<Image> images;
    private Meta meta;
}
