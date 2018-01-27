package me.tomsdevsn.hetznercloud;

import me.tomsdevsn.hetznercloud.objects.general.Server;
import me.tomsdevsn.hetznercloud.objects.request.RequestEnableRescue;
import me.tomsdevsn.hetznercloud.objects.request.RequestServer;
import me.tomsdevsn.hetznercloud.objects.request.RequestServernameChange;
import me.tomsdevsn.hetznercloud.objects.response.*;
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
     * Creates a Cloud-server
     *
     * @param requestServer
     * @return response of the API
     */
    public ResponseServer createServer(RequestServer requestServer) {
        return restTemplate.postForEntity(API_URL + "/servers", new HttpEntity<>(requestServer, httpHeaders), ResponseServer.class).getBody();
    }

    /**
     * Get all of your servers in a list
     *
     * @return the server
     */
    public Servers getServers() {
        return restTemplate.exchange(API_URL + "/servers", HttpMethod.GET, httpEntity, Servers.class).getBody();
    }

    /**
     * Get the server by the name
     *
     * @param name of the server
     * @return the server
     */
    public Servers getServersByName(String name) {
        return restTemplate.exchange(API_URL + "/server?" + name, HttpMethod.GET, httpEntity, Servers.class).getBody();
    }

    /**
     * Get the server by the server-id
     *
     * @param id of the server
     * @returns the server
     */
    public Server getServerById(long id) {
        return restTemplate.exchange(API_URL + "/server/" + id, HttpMethod.GET, httpEntity, Server.class).getBody();
    }

    /**
     * Change the name of the server, in the Hetzner-Cloud Console
     *
     * @param id of the server
     * @param newServerName new server name
     * @return respond
     */
    public ResponseServernameChange changeServerName(int id, RequestServernameChange newServerName) {
        return restTemplate.exchange(API_URL + "/server/" + id, HttpMethod.PUT, new HttpEntity<>(newServerName, httpHeaders), ResponseServernameChange.class).getBody();
    }

    /**
     * Power on a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponsePower powerOnServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/poweron", HttpMethod.POST, httpEntity, ResponsePower.class).getBody();
    }

    /**
     * Reboot a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponsePower softRebootServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/reboot", HttpMethod.POST, httpEntity, ResponsePower.class).getBody();
    }

    /**
     * Reset a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponsePower resetServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/reset", HttpMethod.POST, httpEntity, ResponsePower.class).getBody();
    }

    /**
     * Soft-shutdown a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponsePower shutdownServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/shutdown", HttpMethod.POST, httpEntity, ResponsePower.class).getBody();
    }

    /**
     * Force power off a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponsePower forceShutdownServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/poweroff", HttpMethod.POST, httpEntity, ResponsePower.class).getBody();
    }

    /**
     * Resets the root password of a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResetPassword resetRootPassword(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/reset_password", HttpMethod.POST, httpEntity, ResetPassword.class).getBody();
    }

    /**
     * Enables the rescue mode of the server
     *
     * @param id of the server
     * @return respond
     */
    public ResponseEnableRescue enableRescue(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_rescue", HttpMethod.POST, httpEntity, ResponseEnableRescue.class).getBody();
    }

    /**
     * Enables the rescue mode of the server
     *
     * @param id of the server
     * @param requestEnableRescue
     * @return respond
     */
    public ResponseEnableRescue enableRescue(long id, RequestEnableRescue requestEnableRescue) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_rescue", HttpMethod.POST, new HttpEntity<>(requestEnableRescue, httpHeaders), ResponseEnableRescue.class).getBody();
    }

    /**
     * Disables the rescue mode of the server.
     * Only needed, if you're not in the rescue mode.
     *
     * @param id of the server
     * @return respond
     */
    public ResponseDisableRescue disableRescue(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/disable_rescue", HttpMethod.POST, httpEntity, ResponseDisableRescue.class).getBody();
    }

}