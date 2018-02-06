package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Data;
import me.tomsdevsn.hetznercloud.objects.general.Image;

import java.util.List;

@Data
public class Images {

    public List<Image> images;
    public Meta meta;
}
