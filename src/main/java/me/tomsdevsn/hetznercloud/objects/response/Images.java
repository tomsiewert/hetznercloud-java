package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;
import me.tomsdevsn.hetznercloud.objects.general.Image;

import java.util.List;

@Getter
@Setter
public class Images {

    public List<Image> images;
    public Meta meta;
}
