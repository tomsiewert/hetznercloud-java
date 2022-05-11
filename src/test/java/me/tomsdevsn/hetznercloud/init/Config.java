package me.tomsdevsn.hetznercloud.init;

import me.tomsdevsn.hetznercloud.HetznerCloudAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("#{environment.HCLOUD_TOKEN}")
    private String token;

    @Bean
    public HetznerCloudAPI hetznerCloudAPI(){
        return new HetznerCloudAPI(token);
    }
}
