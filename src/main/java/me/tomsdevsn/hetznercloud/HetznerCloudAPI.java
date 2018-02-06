package me.tomsdevsn.hetznercloud;

import me.tomsdevsn.hetznercloud.objects.request.*;
import me.tomsdevsn.hetznercloud.objects.response.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HetznerCloudAPI {

    private static final String API_URL = "https://api.hetzner.cloud/v1";

    private final String token;

    private HttpEntity<String> httpEntity;
    private HttpHeaders httpHeaders;
    private RestTemplate restTemplate;

    private List<HttpMessageConverter<?>> messageConverters;
    private MappingJackson2HttpMessageConverter converter;

    /**
     * Initial method to use the API
     *
     * @param token which you created in the Hetzner-Cloud console
     */
    public HetznerCloudAPI(String token) {
        this.token = token;

        messageConverters = new ArrayList<>();
        converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        messageConverters.add(converter);

        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        this.httpHeaders.add("Authorization", "Bearer " + token);
        this.httpEntity = new HttpEntity<>("parameters", httpHeaders);

        restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
    }

    /**
     * Get all of your servers in a list
     *
     * @return An object which contains all servers.
     */
    public Servers getServers() {
        return restTemplate.exchange(API_URL + "/servers", HttpMethod.GET, httpEntity, Servers.class).getBody();
    }

    /**
     * Get the server by the name
     *
     * @param name Servername of the server
     * @return     An object which contains the server in a list
     */
    public Servers getServerByName(String name) {
        return restTemplate.exchange(API_URL + "/servers?name=" + name, HttpMethod.GET, httpEntity, Servers.class).getBody();
    }

    /**
     * Get the server by the server-id
     *
     * @param id The id of the server
     * @return   The server with the specific ID
     */
    public ResponseGetServer getServerById(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id, HttpMethod.GET, httpEntity, ResponseGetServer.class).getBody();
    }

    /**
     * Creates a Cloud-server
     *
     * @param requestServer The new server to create.
     * @return              response of the API
     */
    public ResponseServer createServer(RequestServer requestServer) {
        return restTemplate.postForEntity(API_URL + "/servers", new HttpEntity<>(requestServer, httpHeaders), ResponseServer.class).getBody();
    }

    /**
     * Delete a server instantly.
     *
     * @param id Server ID of the server.
     * @return ResponseAction object
     */
    public ResponseAction deleteServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id, HttpMethod.DELETE, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Change the name of the server, in the Hetzner-Cloud Console
     *
     * @param id            of the server
     * @param newServerName request
     * @return              respond
     */
    public ResponseServernameChange changeServerName(long id, RequestServernameChange newServerName) {
        return restTemplate.exchange(API_URL + "/servers/" + id, HttpMethod.PUT, new HttpEntity<>(newServerName, httpHeaders), ResponseServernameChange.class).getBody();
    }

    /**
     * Get all performed Actions for a Server
     *
     * @param id ID of the Server
     * @return ResponseActions object
     */
    public ResponseActions getAllActionsOfServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions", HttpMethod.GET, httpEntity, ResponseActions.class).getBody();
    }

    /**
     * Get a Action for a server
     *
     * @param serverID ID of the Server
     * @param actionID ID of the Action
     * @return ResponseAction object
     */
    public ResponseAction getActionOfServer(long serverID, long actionID) {
        return restTemplate.exchange(API_URL + "/servers/" + serverID + "/actions/" + actionID, HttpMethod.GET, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Get all performed Actions of a Floating IP
     *
     * @param id ID of the FloatingIP
     * @return ResponseActions object
     */
    public ResponseActions getActionsOfFloatingIP(long id) {
        return restTemplate.exchange(API_URL + "/floating_ips/" + id + "/actions", HttpMethod.GET, httpEntity, ResponseActions.class).getBody();
    }

    /**
     * Get a Action for a server
     *
     * @param floatingIPID ID of the Floating IP
     * @param actionID ID of the Action
     * @return ResponseAction object
     */
    public ResponseAction getActionOfFloatingIP(long floatingIPID, long actionID) {
        return restTemplate.exchange(API_URL + "/floating_ips/" + floatingIPID + "/actions/" + actionID, HttpMethod.GET, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Power on a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction powerOnServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/poweron", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Force power off a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction powerOffServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/poweroff", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Reboot a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction softRebootServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/reboot", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Reset a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction resetServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/reset", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Soft-shutdown a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction shutdownServer(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/shutdown", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Resets the root password from a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ResponseResetPassword resetRootPassword(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/reset_password", HttpMethod.POST, httpEntity, ResponseResetPassword.class).getBody();
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id of the server
     * @return respond
     */
    public ResponseEnableRescue enableRescue(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_rescue", HttpMethod.POST, new HttpEntity<>(httpHeaders), ResponseEnableRescue.class).getBody();
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id                  of the server
     * @param requestEnableRescue
     * @return respond
     */
    public ResponseEnableRescue enableRescue(long id, RequestEnableRescue requestEnableRescue) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_rescue", HttpMethod.POST, new HttpEntity<>(requestEnableRescue, httpHeaders), ResponseEnableRescue.class).getBody();
    }

    /**
     * Enables the rescue mode from the server and reset the server
     *
     * @param id of the server
     * @return respond
     */
    public ResponseEnableRescue enableRescueAndReset(long id) {
        ResponseEnableRescue request = restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_rescue", HttpMethod.POST, new HttpEntity<>(httpHeaders), ResponseEnableRescue.class).getBody();
        resetServer(id);
        return request;
    }

    /**
     * Enables the rescue mode from the server and reset the server
     *
     * @param id                  of the server
     * @param requestEnableRescue
     * @return respond
     */
    public ResponseEnableRescue enableRescueAndReset(long id, RequestEnableRescue requestEnableRescue) {
        ResponseEnableRescue request = restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_rescue", HttpMethod.POST, new HttpEntity<>(requestEnableRescue, httpHeaders), ResponseEnableRescue.class).getBody();
        resetServer(id);
        return request;
    }

    /**
     * Disables the rescue mode from the server.
     * Only needed, if the server doesn't booted into the rescue mode.
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction disableRescue(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/disable_rescue", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Get all available Images
     *
     * @return respond
     */
    public Images getImages() {
        return restTemplate.exchange(API_URL + "/images", HttpMethod.GET, httpEntity, Images.class).getBody();
    }

    /**
     * Rebuild a server, with the specific image.
     * example: ubuntu-16.04
     *
     * @param id                   of the server
     * @param requestRebuildServer
     * @return respond
     */
    public ResponseRebuildServer rebuildServer(long id, RequestRebuildServer requestRebuildServer) {
        return restTemplate.postForEntity(API_URL + "/servers/" + id + "/actions/rebuild", new HttpEntity<>(requestRebuildServer, httpHeaders), ResponseRebuildServer.class).getBody();
    }

    /**
     * Change the type from the server
     * example: cx11 to cx21
     *
     * Attention: It will stops the server, but it starts automatically after the upgrade
     *
     * @param id of the server
     * @param requestChangeType
     * @return respond
     */
    public ResponseAction changeServerType(long id, RequestChangeType requestChangeType) throws InterruptedException {
        this.powerOffServer(id);
        TimeUnit.SECONDS.sleep(7);
        return restTemplate.postForEntity(API_URL + "/servers/" + id + "/actions/change_type", new HttpEntity<>(requestChangeType, httpHeaders), ResponseAction.class).getBody();
    }

    /**
     * Get the metrics from a server
     *
     * @param id         of the server
     * @param metricType like cpu, disk or network (but also cpu,disk possible)
     * @param start      of the metric
     * @param end        of the metric
     * @return respond
     */
    public ResponseMetrics getMetrics(long id, String metricType, String start, String end) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/metrics?type=" + metricType + "&start=" + start + "&end=" + end, HttpMethod.GET, httpEntity, ResponseMetrics.class).getBody();
    }

    /**
     * Create a image from a server
     *
     * @param id                 of the server
     * @param requestCreateImage
     * @return respond
     */
    public ResponseCreateImage createImage(long id, RequestCreateImage requestCreateImage) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/create_image", HttpMethod.POST, new HttpEntity<>(requestCreateImage, httpHeaders), ResponseCreateImage.class).getBody();
    }

    /**
     * Enable the backups from a server
     * Please reminder, that will increase the price of the server by 20%
     *
     * @param id                  of the server
     * @param requestEnableBackup
     * @return respond
     */
    public ResponseEnableBackup enableBackup(long id, RequestEnableBackup requestEnableBackup) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/enable_backup", HttpMethod.POST, new HttpEntity<>(requestEnableBackup, httpHeaders), ResponseEnableBackup.class).getBody();
    }

    /**
     * Disable the backups from a server
     * Caution!: This will delete all existing backups immediately
     *
     * @param id of the server
     * @return respond
     */
    public ResponseDisableBackup disableBackup(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/disable_backup", HttpMethod.POST, httpEntity, ResponseDisableBackup.class).getBody();
    }

    /**
     * Get all available ISO's.
     *
     * @return respond
     */
    public ResponseISOS getISOS() {
        return restTemplate.exchange(API_URL + "/isos", HttpMethod.GET, httpEntity, ResponseISOS.class).getBody();
    }

    /**
     * Get an ISO by ID
     *
     * @param id ID of the ISO
     * @return ResponseISO Object
     */
    public ResponseISO getISOById(long id) {
        return restTemplate.exchange(API_URL + "/isos/" + id, HttpMethod.GET, httpEntity, ResponseISO.class).getBody();
    }

    /**
     * Attach an ISO to a server.
     * <p>
     * To get all ISO's {@link #getISOS}
     *
     * @param id               of the server
     * @param requestAttachISO Request object
     * @return ResponseAction object
     */
    public ResponseAction attachISO(long id, RequestAttachISO requestAttachISO) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/attach_iso", HttpMethod.POST, new HttpEntity<>(requestAttachISO, httpHeaders), ResponseAction.class).getBody();
    }

    /**
     * Detach an ISO from a server.
     *
     * @param id of the server
     * @return respond
     */
    public ResponseAction detachISO(long id) {
        return restTemplate.exchange(API_URL + "/servers/" + id + "/actions/detach_iso", HttpMethod.POST, httpEntity, ResponseAction.class).getBody();
    }

    /**
     * Changes the reverse DNS entry from a server.
     * <p>
     * Floating IPs assigned to the server are not affected!
     *
     * @param id                  of the server
     * @param requestChangeReverseDNS
     * @return respond
     */
    public ResponseAction changeDNSPTR(long id, RequestChangeReverseDNS requestChangeReverseDNS) {
        return restTemplate.postForEntity(API_URL + "/servers/" + id + "/actions/change_dns_ptr", new HttpEntity<>(requestChangeReverseDNS, httpHeaders),
                                          ResponseAction.class).getBody();
    }

    /**
     * Get a Datacenter by ID
     *
     * @param id of the datacenter
     * @return respond
     */
    public ResponseDatacenter getDatacenter(long id) {
        return restTemplate.exchange(API_URL + "/datacenters/" + id, HttpMethod.GET, httpEntity, ResponseDatacenter.class).getBody();
    }

    /**
     * Get all available datacenters and the recommendation
     *
     * @return respond
     */
    public ResponseDatacenters getDatacenters() {
        return restTemplate.exchange(API_URL + "/datacenters", HttpMethod.GET, httpEntity, ResponseDatacenters.class).getBody();
    }

    /**
     * Get a datacenter by name
     *
     * @param name of the datacenter
     * @return respond
     */
    public ResponseDatacenters getDatacenter(String name) {
        return restTemplate.exchange(API_URL + "/datacenters?" + name, HttpMethod.GET, httpEntity, ResponseDatacenters.class).getBody();
    }

    /**
     * Get all prices from the products
     *
     * @return respond
     */
    public ResponsePricing getPricing() {
        return restTemplate.exchange(API_URL + "/pricing", HttpMethod.GET, httpEntity, ResponsePricing.class).getBody();
    }

    /**
     * Get all Floating IP's in a object
     *
     * @return ResponseFloatingIPs object
     */
    public ResponseFloatingIPs getFloatingIPs() {
        return restTemplate.exchange(API_URL + "/floating_ips", HttpMethod.GET, httpEntity, ResponseFloatingIPs.class).getBody();
    }

    /**
     * Get a specific Floating IP.
     *
     * @param id ID of the Floating IP
     * @return ResponseGetFloatingIP object
     */
    public ResponseGetFloatingIP getFloatingIP(long id) {
        return restTemplate.exchange(API_URL + "/floating_ips/" + id, HttpMethod.GET, httpEntity, ResponseGetFloatingIP.class).getBody();
    }

    /**
     * Create a Floating IP for the project or for a Server.
     *
     * @param requestFloatingIP Request object
     * @return ResponseFloatingIP object
     */
    public ResponseFloatingIP createFloatingIP(RequestFloatingIP requestFloatingIP) {
        return restTemplate.postForEntity(API_URL + "/floating_ips", new HttpEntity<>(requestFloatingIP, httpHeaders), ResponseFloatingIP.class).getBody();
    }

    /**
     * Change the description of a Floating IP.
     *
     * @param id ID of the Floating IP
     * @param requestDescriptionFloatingIP Request object
     * @return ResponseGetFloatingIP object
     */
    public ResponseGetFloatingIP changeDescriptionOfFloatingIP(long id, RequestDescriptionFloatingIP requestDescriptionFloatingIP) {
        return restTemplate.exchange(API_URL + "/floating_ips/" + id, HttpMethod.PUT, new HttpEntity<>(requestDescriptionFloatingIP, httpHeaders),
                                         ResponseGetFloatingIP.class).getBody();
    }

    /**
     * Assign a Floating IP to a server
     *
     * @param id ID of the Floating IP
     * @param requestAssignFloatingIP Request object
     * @return ResponseAction object
     */
    public ResponseAction assignFloatingIP(long id, RequestAssignFloatingIP requestAssignFloatingIP) {
        return restTemplate.postForEntity(API_URL + "/floating_ips/" + id + "/actions/assign", new HttpEntity<>(requestAssignFloatingIP, httpHeaders),
                                          ResponseAction.class).getBody();
    }

    /**
     * Unassign a Floating IP from a server
     *
     * @param id ID of the Floating IP
     * @return ResponseAction object
     */
    public ResponseAction unassignFloatingIP(long id) {
        return restTemplate.postForEntity(API_URL + "/floating_ips/" + id + "/actions/unassign", new HttpEntity<>(httpHeaders),
                ResponseAction.class).getBody();
    }

    /**
     * Change the reverse DNS entry for a Floating IP
     *
     * @param id ID of the Floating IP
     * @param requestChangeReverseDNS Request object
     * @return ResponseAction object
     */
    public ResponseAction changeFloatingReverseDNS(long id, RequestChangeReverseDNS requestChangeReverseDNS) {
        return restTemplate.postForEntity(API_URL + "/floating_ips/" + id + "/actions/change_dns_ptr", new HttpEntity<>(requestChangeReverseDNS, httpHeaders),
                                          ResponseAction.class).getBody();
    }

    /**
     * Delete a Floating IP.
     *
     * This object does not have a respond!
     *
     * @param id ID of the Floating ID
     * @return
     */
    public String deleteFloatingIP(long id) {
        return restTemplate.exchange(API_URL + "/floating_ips/" + id, HttpMethod.DELETE, httpEntity, String.class).getBody();
    }

    /**
     * Converts a Date to the ISO-8601 format
     *
     * @param date your Date
     * @return Date in ISO-8601 format
     */
    public String convertToISO8601(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatted = dateFormat.format(date);
        return formatted;
    }
}