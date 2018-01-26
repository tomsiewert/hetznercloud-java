package me.tomsdevsn.hetznercloud;

import me.tomsdevsn.hetznercloud.objects.general.Server;
import me.tomsdevsn.hetznercloud.objects.response.Error;
import me.tomsdevsn.hetznercloud.objects.response.Servers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HetznerCloudAPI {

    private static final String API_URL = "https://api.hetzner.cloud/v1";

    private final String token;

    private HttpEntity<String> httpEntity;
    private HttpHeaders httpHeaders;
    private final RestTemplate restTemplate;
    private List<HttpMessageConverter<?>> messageConverters;
    private MappingJackson2HttpMessageConverter converter;

    /**
     * Initial method to use the API
     * @param token
     */
    public HetznerCloudAPI(String token) {
        this.token = token;

        restTemplate = new RestTemplate();
        messageConverters = new ArrayList<HttpMessageConverter<?>>();
        converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(new MediaType[]{MediaType.ALL}));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", "Bearer " + token);
        httpEntity = new HttpEntity<>("parameters", httpHeaders);
    }

    /**
     * @returns the Server
     */
    public Servers getServers() {
        return restTemplate.exchange(API_URL + "/servers", HttpMethod.GET, httpEntity, Servers.class).getBody();
    }

    /**
     * @param name
     * @returns the Server
     */
    public Servers getServersByName(String name) {
        return restTemplate.exchange(API_URL + "/server?" + name, HttpMethod.GET, httpEntity, Servers.class).getBody();
    }

    /**
     * @param id
     * @returns the Server
     */
    public Server getServerById(long id) {
        return restTemplate.exchange(API_URL + "/server/" + id, HttpMethod.GET, httpEntity, Server.class).getBody();
    }

}