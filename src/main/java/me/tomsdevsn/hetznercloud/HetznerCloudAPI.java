package me.tomsdevsn.hetznercloud;

import me.tomsdevsn.hetznercloud.exception.InvalidFormatException;
import me.tomsdevsn.hetznercloud.exception.InvalidParametersException;
import me.tomsdevsn.hetznercloud.objects.general.Action;
import me.tomsdevsn.hetznercloud.objects.general.PlacementGroup;
import me.tomsdevsn.hetznercloud.objects.general.PlacementGroupType;
import me.tomsdevsn.hetznercloud.objects.request.*;
import me.tomsdevsn.hetznercloud.objects.response.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HetznerCloudAPI {

    private static final String API_URL = "https://api.hetzner.cloud/v1";

    private final String token;

    private final HttpEntity<String> httpEntity;
    private final HttpHeaders httpHeaders;
    private final RestTemplate restTemplate;

    private final List<HttpMessageConverter<?>> messageConverters;
    private final MappingJackson2HttpMessageConverter converter;

    /**
     * Initial method to use the API
     *
     *
     * @param token which you created in the Hetzner-Cloud console
     */
    public HetznerCloudAPI(String token) {
        this.token = token;

        messageConverters = new ArrayList<>();
        converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
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
        return restTemplate.exchange(
                API_URL + "/servers",
                HttpMethod.GET,
                httpEntity,
                Servers.class).getBody();
    }

    /**
     * Get the server by the name
     *
     * @param name Servername of the server
     * @return An object which contains the server in a list
     */
    public Servers getServerByName(String name) {
        return restTemplate.exchange(
                API_URL + "/servers?name=" + name,
                HttpMethod.GET,
                httpEntity,
                Servers.class).getBody();
    }

    /**
     * Get the server by the server-id
     *
     * @param id The id of the server
     * @return The server with the specific ID
     */
    public GetServerResponse getServerById(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id,
                HttpMethod.GET,
                httpEntity,
                GetServerResponse.class).getBody();
    }

    /**
     * Creates a Cloud-server
     *
     * @param serverRequest The new server to create.
     * @return response of the API
     */
    public ServerResponse createServer(ServerRequest serverRequest) {
        if (serverRequest.getSshKeys() != null) {
            serverRequest.getSshKeys().forEach(object -> {
                if (!(object instanceof Long || object instanceof String))
                    throw new InvalidParametersException("Object not Long or String");
            });
        }
        serverRequest.setServerType(serverRequest.getServerType().toLowerCase());   // Case-sensitive fix
        return restTemplate.postForEntity(
                API_URL + "/servers",
                new HttpEntity<>(serverRequest, httpHeaders),
                ServerResponse.class).getBody();
    }

    /**
     * Delete a server instantly.
     *
     * @param id Server ID of the server.
     * @return ActionResponse object
     */
    public ActionResponse deleteServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id,
                HttpMethod.DELETE,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Change the name of the server, in the Hetzner-Cloud Console
     *
     * @param id            of the server
     * @param newServerName request
     * @return respond
     */
    public ServernameChangeResponse changeServerName(long id, ServernameChangeRequest newServerName) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(newServerName, httpHeaders),
                ServernameChangeResponse.class).getBody();
    }

    /**
     * Request a VNC over Websocket-console
     *
     * @param id ID of the server
     * @return ConsoleResponse object
     */
    public ConsoleResponse requestConsole(long id) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/request_console",
                httpEntity,
                ConsoleResponse.class).getBody();
    }

    /**
     * Change the protection configuration from a server
     *
     * @param id               ID of the server
     * @param changeProtection Request Object (both optional)
     * @return ActionResponse object
     */
    public ActionResponse changeServerProtection(long id, ChangeProtectionRequest changeProtection) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/change_protection",
                new HttpEntity<>(changeProtection, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Add a server to a placement group.
     *
     * Server has to be stopped.
     *
     * @param serverId server id
     * @param placementGroupId placement group id
     * @return ActionResponse
     */
    public ActionResponse addServerToPlacementGroup(long serverId, long placementGroupId) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + serverId + "/actions/add_to_placement_group",
                new HttpEntity<>("{\"placement_group\": " + placementGroupId + "}", httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Remove a server from a placement group.
     *
     * @param serverId server id
     * @return ActionResponse
     */
    public ActionResponse removeServerFromPlacementGroup(long serverId) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + serverId + "/actions/remove_from_placement_group",
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Get all performed Actions for a Server
     *
     * @param id ID of the Server
     * @return ActionsResponse object
     */
    public ActionsResponse getAllActionsOfServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions",
                HttpMethod.GET,
                httpEntity,
                ActionsResponse.class).getBody();
    }

    /**
     * Get an action for a server
     *
     * @param serverID ID of the Server
     * @param actionID ID of the Action
     * @return ActionResponse object
     */
    public ActionResponse getActionOfServer(long serverID, long actionID) {
        return restTemplate.exchange(
                API_URL + "/servers/" + serverID + "/actions/" + actionID,
                HttpMethod.GET,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Get all performed Actions of a Floating IP
     *
     * @param id ID of the FloatingIP
     * @return ActionsResponse object
     */
    public ActionsResponse getActionsOfFloatingIP(long id) {
        return restTemplate.exchange(API_URL + "/floating_ips/" + id + "/actions",
                HttpMethod.GET,
                httpEntity,
                ActionsResponse.class).getBody();
    }

    /**
     * Get an action for a server
     *
     * @param floatingIPID ID of the Floating IP
     * @param actionID     ID of the Action
     * @return ActionResponse object
     */
    public ActionResponse getActionOfFloatingIP(long floatingIPID, long actionID) {
        return restTemplate.exchange(
                API_URL + "/floating_ips/" + floatingIPID + "/actions/" + actionID,
                HttpMethod.GET,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Power on a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse powerOnServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/poweron",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Force power off a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse powerOffServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/poweroff",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Reboot a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse softRebootServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/reboot",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Reset a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse resetServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/reset",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Soft-shutdown a specific server with the id
     *
     * @param id ID of the server
     * @return respond
     */
    public ActionResponse shutdownServer(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/shutdown",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Resets the root password from a specific server with the id
     *
     * @param id ID of the server
     * @return respond
     */
    public ResetRootPasswordResponse resetRootPassword(long id) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/reset_password",
                new HttpEntity<>(httpHeaders),
                ResetRootPasswordResponse.class).getBody();
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id ID of the server
     * @return respond
     */
    public EnableRescueResponse enableRescue(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                EnableRescueResponse.class).getBody();
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id                  ID of the server
     * @param enableRescueRequest Request object
     * @return respond
     */
    public EnableRescueResponse enableRescue(long id, EnableRescueRequest enableRescueRequest) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                HttpMethod.POST,
                new HttpEntity<>(enableRescueRequest, httpHeaders),
                EnableRescueResponse.class).getBody();
    }

    /**
     * Enables the rescue mode from the server and reset the server
     *
     * @param id ID of the server
     * @return respond
     */
    public EnableRescueResponse enableRescueAndReset(long id) {
        EnableRescueResponse request = restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                HttpMethod.POST, new HttpEntity<>(httpHeaders),
                EnableRescueResponse.class).getBody();
        resetServer(id);
        return request;
    }

    /**
     * Enables the rescue mode from the server and reset the server
     *
     * @param id                  ID of the server
     * @param enableRescueRequest Request object
     * @return respond
     */
    public EnableRescueResponse enableRescueAndReset(long id, EnableRescueRequest enableRescueRequest) {
        EnableRescueResponse request = restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                HttpMethod.POST,
                new HttpEntity<>(enableRescueRequest, httpHeaders),
                EnableRescueResponse.class).getBody();
        resetServer(id);
        return request;
    }

    /**
     * Disables the rescue mode from the server.
     *
     * Only needed, if the server doesn't booted into the rescue mode.
     *
     * @param id ID of the server
     * @return respond
     */
    public ActionResponse disableRescue(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/disable_rescue",
                HttpMethod.POST,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Rebuild a server, with the specific image.
     *
     * example: ubuntu-16.04
     *
     * @param id                   ID of the server
     * @param rebuildServerRequest Request object
     * @return respond
     */
    public RebuildServerResponse rebuildServer(long id, RebuildServerRequest rebuildServerRequest) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/rebuild",
                new HttpEntity<>(rebuildServerRequest, httpHeaders),
                RebuildServerResponse.class).getBody();
    }

    /**
     * Change the type from the server
     *
     * example: cx11 to cx21
     *
     * Attention: It will stops the server, but it starts automatically after the upgrade
     *
     * @param id                ID of the server
     * @param changeTypeRequest Request object
     * @return respond
     * @throws java.lang.InterruptedException because there is a timeout
     */
    public ActionResponse changeServerType(long id, ChangeTypeRequest changeTypeRequest) throws InterruptedException {
        this.powerOffServer(id);
        TimeUnit.SECONDS.sleep(7);
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/change_type",
                new HttpEntity<>(changeTypeRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Get the metrics from a server
     *
     * @param id         ID of the server
     * @param metricType like cpu, disk or network (but also cpu,disk possible)
     * @param start      of the metric
     * @param end        of the metric
     * @return respond
     */
    public MetricsResponse getMetrics(long id, String metricType, String start, String end) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/metrics?type=" + metricType + "&start=" + start + "&end=" + end,
                HttpMethod.GET,
                httpEntity,
                MetricsResponse.class).getBody();
    }

    /**
     * Create a image from a server
     *
     * @param id                 ID of the server
     * @param createImageRequest Request object
     * @return respond
     */
    public CreateImageResponse createImage(long id, CreateImageRequest createImageRequest) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/create_image",
                HttpMethod.POST,
                new HttpEntity<>(createImageRequest, httpHeaders),
                CreateImageResponse.class).getBody();
    }


    /**
     * Enable or disable the Protection of an Image
     *
     * @param id                ID of the image
     * @param protectionRequest Only the delete parameter!
     * @return ActionResponse object
     */
    public ActionResponse changeImageProtection(long id, ChangeProtectionRequest protectionRequest) {
        return restTemplate.postForEntity(
                API_URL + "/images/" + id + "/actions/change_protection",
                new HttpEntity<>(protectionRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * @param id                  ID of the server
     * @param enableBackupRequest Request object
     * @return respone
     * @deprecated due Hetzner changed Request header
     *
     * Enable the backups from a server
     *
     * Please reminder, that will increase the price of the server by 20%
     */
    @Deprecated
    public EnableBackupResponse enableBackup(long id, EnableBackupRequest enableBackupRequest) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/enable_backup",
                HttpMethod.POST,
                new HttpEntity<>(enableBackupRequest, httpHeaders),
                EnableBackupResponse.class).getBody();
    }

    /**
     * Enable the backups from a server
     *
     * Please reminder, that will increase the price of the server by 20%
     *
     * @param id ID of the server
     * @return respone
     */
    public ActionResponse enableBackup(long id) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/enable_backup",
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Disable the backups from a server
     *
     * Caution!: This will delete all existing backups immediately
     *
     * @param id ID of the server
     * @return respond
     */
    public DisableBackupResponse disableBackup(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/disable_backup",
                HttpMethod.POST,
                httpEntity,
                DisableBackupResponse.class).getBody();
    }

    /**
     * Get all available ISO's.
     *
     * @return respond
     */
    public ISOSResponse getISOS() {
        return restTemplate.exchange(
                API_URL + "/isos",
                HttpMethod.GET,
                httpEntity,
                ISOSResponse.class).getBody();
    }

    /**
     * Get an ISO by ID
     *
     * @param id ID of the ISO
     * @return ISOResponse Object
     */
    public ISOResponse getISOById(long id) {
        return restTemplate.exchange(
                API_URL + "/isos/" + id,
                HttpMethod.GET,
                httpEntity,
                ISOResponse.class).getBody();
    }

    /**
     * Attach an ISO to a server.
     *
     * To get all ISO's {@link #getISOS}
     *
     * @param id               of the server
     * @param attachISORequest Request object
     * @return ActionResponse object
     */
    public ActionResponse attachISO(long id, AttachISORequest attachISORequest) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/attach_iso",
                HttpMethod.POST,
                new HttpEntity<>(attachISORequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Detach an ISO from a server.
     *
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse detachISO(long id) {
        return restTemplate.exchange(
                API_URL + "/servers/" + id + "/actions/detach_iso",
                HttpMethod.POST,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Changes the reverse DNS entry from a server.
     *
     * Floating IPs assigned to the server are not affected!
     *
     * @param id                      ID of the server
     * @param changeReverseDNSRequest Request object
     * @return respond
     */
    public ActionResponse changeDNSPTR(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/change_dns_ptr",
                new HttpEntity<>(changeReverseDNSRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Get a Datacenter by ID
     *
     *
     * @param id of the Datacenter
     * @return respond
     */
    public DatacenterResponse getDatacenter(long id) {
        return restTemplate.exchange(
                API_URL + "/datacenters/" + id,
                HttpMethod.GET,
                httpEntity,
                DatacenterResponse.class).getBody();
    }

    /**
     * Get all available datacenters and the recommendation
     *
     *
     * @return respond
     */
    public DatacentersResponse getDatacenters() {
        return restTemplate.exchange(
                API_URL + "/datacenters",
                HttpMethod.GET,
                httpEntity,
                DatacentersResponse.class).getBody();
    }

    /**
     * Get a datacenter by name
     *
     *
     * @param name of the datacenter
     * @return respond
     */
    public DatacentersResponse getDatacenter(String name) {
        return restTemplate.exchange(
                API_URL + "/datacenters?" + name,
                HttpMethod.GET,
                httpEntity,
                DatacentersResponse.class).getBody();
    }

    /**
     * Get all prices from the products
     *
     *
     * @return respond
     */
    public PricingResponse getPricing() {
        return restTemplate.exchange(
                API_URL + "/pricing",
                HttpMethod.GET,
                httpEntity,
                PricingResponse.class).getBody();
    }

    /**
     * Get all Floating IP's in a object
     *
     *
     * @return FloatingIPsResponse object
     */
    public FloatingIPsResponse getFloatingIPs() {
        return restTemplate.exchange(
                API_URL + "/floating_ips",
                HttpMethod.GET,
                httpEntity,
                FloatingIPsResponse.class).getBody();
    }

    /**
     * Get a specific Floating IP.
     *
     *
     * @param id ID of the Floating IP
     * @return GetFloatingIPResponse object
     */
    public GetFloatingIPResponse getFloatingIP(long id) {
        return restTemplate.exchange(
                API_URL + "/floating_ips/" + id,
                HttpMethod.GET,
                httpEntity,
                GetFloatingIPResponse.class).getBody();
    }

    /**
     * Create a Floating IP for the project or for a Server.
     *
     *
     * @param floatingIPRequest Request object
     * @return FloatingIPResponse object
     */
    public FloatingIPResponse createFloatingIP(FloatingIPRequest floatingIPRequest) {
        return restTemplate.postForEntity(
                API_URL + "/floating_ips",
                new HttpEntity<>(floatingIPRequest, httpHeaders),
                FloatingIPResponse.class).getBody();
    }

    /**
     * Enable or disable the Protection of a Floating IP
     *
     * @param id                ID of the Floating IP
     * @param protectionRequest Only the delete parameter!
     * @return ActionResponse object
     */
    public ActionResponse changeFloatingIPProtection(long id, ChangeProtectionRequest protectionRequest) {
        return restTemplate.postForEntity(
                API_URL + "/floating_ips/" + id + "/actions/change_protection",
                new HttpEntity<>(protectionRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Change the description of a Floating IP.
     *
     *
     * @param id                           ID of the Floating IP
     * @param descriptionFloatingIPRequest Request object
     * @return GetFloatingIPResponse object
     */
    public GetFloatingIPResponse changeDescriptionOfFloatingIP(long id, DescriptionFloatingIPRequest descriptionFloatingIPRequest) {
        return restTemplate.exchange(
                API_URL + "/floating_ips/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(descriptionFloatingIPRequest, httpHeaders),
                GetFloatingIPResponse.class).getBody();
    }

    /**
     * Assign a Floating IP to a server
     *
     *
     * @param id                      ID of the Floating IP
     * @param assignFloatingIPRequest Request object
     * @return ActionResponse object
     */
    public ActionResponse assignFloatingIP(long id, AssignFloatingIPRequest assignFloatingIPRequest) {
        return restTemplate.postForEntity(
                API_URL + "/floating_ips/" + id + "/actions/assign",
                new HttpEntity<>(assignFloatingIPRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Unassign a Floating IP from a server
     *
     *
     * @param id ID of the Floating IP
     * @return ActionResponse object
     */
    public ActionResponse unassignFloatingIP(long id) {
        return restTemplate.postForEntity(
                API_URL + "/floating_ips/" + id + "/actions/unassign",
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Change the reverse DNS entry for a Floating IP
     *
     *
     * @param id                      ID of the Floating IP
     * @param changeReverseDNSRequest Request object
     * @return ActionResponse object
     */
    public ActionResponse changeFloatingReverseDNS(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return restTemplate.postForEntity(
                API_URL + "/floating_ips/" + id + "/actions/change_dns_ptr",
                new HttpEntity<>(changeReverseDNSRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Delete a Floating IP.
     *
     * This object does not have a respond!
     *
     *
     * @param id ID of the Floating ID
     * @return String
     */
    public String deleteFloatingIP(long id) {
        return restTemplate.exchange(
                API_URL + "/floating_ips/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Update the description or labels of a Floating IP.
     *
     * @param id                      ID of the Floating IP
     * @param updateFloatingIPRequest Request Object
     * @return Response from API (Action will be null)
     */
    public FloatingIPResponse updateFloatingIP(long id, UpdateFloatingIPRequest updateFloatingIPRequest) {
        return restTemplate.exchange(
                API_URL + "/floating_ips/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateFloatingIPRequest, httpHeaders),
                FloatingIPResponse.class).getBody();
    }

    /**
     * Get all SSH keys.
     *
     *
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKeys() {
        return restTemplate.exchange(
                API_URL + "/ssh_keys",
                HttpMethod.GET,
                httpEntity,
                SSHKeysResponse.class).getBody();
    }

    /**
     * Get a SSH key by ID.
     *
     *
     * @param id ID of the SSH key
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse getSSHKey(long id) {
        return restTemplate.exchange(
                API_URL + "/ssh_keys/" + id,
                HttpMethod.GET,
                httpEntity,
                SSHKeyResponse.class).getBody();
    }

    /**
     * Get a SSH key by name.
     *
     *
     * @param name name of the SSH key
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKeyByName(String name) {
        return restTemplate.exchange(
                API_URL + "/ssh_keys?" + name,
                HttpMethod.GET,
                httpEntity,
                SSHKeysResponse.class).getBody();
    }

    /**
     * Get a SSH key by the fingerprint.
     *
     * @param fingerprint Fingerprint of the SSH key
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKeyByFingerprint(String fingerprint) {
        return restTemplate.exchange(
                API_URL + "/ssh_keys?" + fingerprint,
                HttpMethod.GET,
                httpEntity,
                SSHKeysResponse.class).getBody();
    }

    /**
     * Create a SSH key.
     *
     *
     * @param SSHKeyRequest Request object
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse createSSHKey(SSHKeyRequest SSHKeyRequest) {
        return restTemplate.postForEntity(
                API_URL + "/ssh_keys",
                new HttpEntity<>(SSHKeyRequest, httpHeaders),
                SSHKeyResponse.class).getBody();
    }

    /**
     * Change the name of a SSH key
     *
     *
     * @param id                      ID of the SSH key
     * @param changeSSHKeyNameRequest Request object
     * @return SSHKeyResponse object
     * @deprecated use {@link HetznerCloudAPI#updateSSHKey(long, UpdateSSHKeyRequest)} instead
     */
    @Deprecated
    public SSHKeyResponse changeSSHKeyName(long id, ChangeSSHKeyNameRequest changeSSHKeyNameRequest) {
        return restTemplate.exchange(
                API_URL + "/ssh_keys/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(changeSSHKeyNameRequest, httpHeaders),
                SSHKeyResponse.class).getBody();
    }

    /**
     * Update parameters of a SSH key
     *
     *
     * @param id                  ID of the SSH key
     * @param updateSSHKeyRequest Request Object
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse updateSSHKey(long id, UpdateSSHKeyRequest updateSSHKeyRequest) {
        return restTemplate.exchange(
                API_URL + "/ssh_keys/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateSSHKeyRequest, httpHeaders),
                SSHKeyResponse.class).getBody();
    }

    /**
     * Delete a SSH key.
     *
     * This object does not have a respond!
     *
     *
     * @param id ID of the SSH key
     * @return String
     */
    public String deleteSSHKey(long id) {
        return restTemplate.exchange(
                API_URL + "/ssh_keys/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Get all Server types.
     *
     *
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypes() {
        return restTemplate.exchange(
                API_URL + "/server_types",
                HttpMethod.GET,
                httpEntity,
                ServerTypesResponse.class).getBody();
    }


    /**
     * Get all Load Balancer types.
     *
     *
     * @return ServerTypesResponse object
     */
    public LoadBalancerTypeResponse getAllLoadBalancerTypes() {
        return restTemplate.exchange(
                API_URL + "/load_balancer_types",
                HttpMethod.GET,
                httpEntity,
                LoadBalancerTypeResponse.class).getBody();
    }

    /**
     * Get all Load Balancer types.
     *
     *
     * @return ServerTypesResponse object
     */
    public LoadBalancerTypeResponse getAllLoadBalancerTypesByName(String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL + "/load_balancer_types")
                .queryParam("name", name);

        return restTemplate.exchange(
                builder.toUriString() ,
                HttpMethod.GET,
                httpEntity,
                LoadBalancerTypeResponse.class).getBody();
    }


    /**
     * Get a Server type by name.
     *
     *
     * @param name name of the Server type
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypeByName(String name) {
        return restTemplate.exchange(
                API_URL + "/server_types?name=" + name,
                HttpMethod.GET,
                httpEntity,
                ServerTypesResponse.class).getBody();
    }

    /**
     * Create a SSH key.
     *
     *
     * @param id ID of the Server type
     * @return ServerTypeResponse object
     */
    public ServerTypeResponse getServerType(long id) {
        return restTemplate.exchange(
                API_URL + "/server_types/" + id,
                HttpMethod.GET,
                httpEntity,
                ServerTypeResponse.class).getBody();
    }

    /**
     * Get all available Locations.
     *
     *
     * @return LocationsResponse object
     */
    public LocationsResponse getLocations() {
        return restTemplate.exchange(
                API_URL + "/locations",
                HttpMethod.GET,
                httpEntity,
                LocationsResponse.class).getBody();
    }

    /**
     * Get a Location by name.
     *
     *
     * @param name Name of the location
     * @return LocationsResponse object
     */
    public LocationsResponse getLocationByName(String name) {
        return restTemplate.exchange(
                API_URL + "/locations?name=" + name,
                HttpMethod.GET,
                httpEntity,
                LocationsResponse.class).getBody();
    }

    /**
     * Get a location;
     *
     *
     * @param id ID of the location
     * @return ServerTypeResponse object
     */
    public LocationResponse getLocation(long id) {
        return restTemplate.exchange(
                API_URL + "/locations/" + id,
                HttpMethod.GET,
                httpEntity,
                LocationResponse.class).getBody();
    }

    /**
     * Get all available ImagesResponse
     *
     *
     * @return respond
     */
    public ImagesResponse getImages() {
        return restTemplate.exchange(
                API_URL + "/images",
                HttpMethod.GET,
                httpEntity,
                ImagesResponse.class).getBody();
    }

    /**
     * Get all images by type.
     *
     *
     * @param type Type of the images
     * @return ImagesResponse object
     */
    public ImagesResponse getImages(ImageType type) {
        return restTemplate.exchange(
                API_URL + "/images?type=" + type.toString(),
                HttpMethod.GET,
                httpEntity,
                ImagesResponse.class).getBody();
    }

    /**
     * Get an image by name.
     *
     *
     * @param name Name of the image
     * @return ImagesResponse object
     */
    public ImagesResponse getImageByName(String name) {
        return restTemplate.exchange(
                API_URL + "/images?name=" + name,
                HttpMethod.GET,
                httpEntity,
                ImagesResponse.class).getBody();
    }

    /**
     * Get image by ID.
     *
     *
     * @param id ID of the image
     * @return ImageResponse object
     */
    public ImageResponse getImage(long id) {
        return restTemplate.exchange(
                API_URL + "/images/" + id,
                HttpMethod.GET,
                httpEntity,
                ImageResponse.class).getBody();
    }

    /**
     * Update the description or the type of a image.
     *
     *
     * @param id                 ID of the image
     * @param updateImageRequest Request object
     * @return ImageResponse object
     */
    public ImageResponse updateImage(long id, UpdateImageRequest updateImageRequest) {
        return restTemplate.exchange(
                API_URL + "/images/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateImageRequest, httpHeaders),
                ImageResponse.class).getBody();
    }

    /**
     * Delete an image,
     *
     * This object does not have a respond!
     *
     *
     * @param id ID of the image
     * @return String
     */
    public String deleteImage(long id) {
        return restTemplate.exchange(
                API_URL + "/images/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Get all available volumes from your project.
     *
     * @return Volume Array
     */
    public GetVolumesResponse getVolumes() {
        return restTemplate.exchange(
                API_URL + "/volumes",
                HttpMethod.GET,
                httpEntity,
                GetVolumesResponse.class).getBody();
    }

    /**
     * Get a specific volume from your project by id.
     *
     * @param id ID of the volume
     * @return Volume object
     */
    public GetVolumeResponse getVolume(long id) {
        return restTemplate.exchange(
                API_URL + "/volumes/" + id,
                HttpMethod.GET,
                httpEntity,
                GetVolumeResponse.class).getBody();
    }

    /**
     * Create a new volume.
     *
     * @param volumeRequest Volume request object
     * @return Volume object with action
     */
    public VolumeResponse createVolume(VolumeRequest volumeRequest) {
        if ((volumeRequest.getFormat() != null))
            volumeRequest.setFormat(volumeRequest.getFormat().toLowerCase());   // case-sensitive fix
        return restTemplate.postForEntity(
                API_URL + "/volumes",
                new HttpEntity<>(volumeRequest, httpHeaders),
                VolumeResponse.class).getBody();
    }

    /**
     * Update some specific options of a volume.
     *
     * @param id                  ID of the volume
     * @param updateVolumeRequest Update volume request object
     * @return GetVolume object
     */
    public GetVolumeResponse updateVolume(long id, UpdateVolumeRequest updateVolumeRequest) {
        if (updateVolumeRequest.getName() == null) throw new InvalidParametersException("Name must be specified");
        return restTemplate.exchange(
                API_URL + "/volumes/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateVolumeRequest, httpHeaders),
                GetVolumeResponse.class).getBody();
    }

    /**
     * Delete a volume
     *
     * @param id ID of the volume
     * @return no return object
     */
    public String deleteVolume(long id) {
        return restTemplate.exchange(
                API_URL + "/volumes/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Get all actions of a volume.
     *
     * @param id ID of the volume
     * @return Action array
     */
    public ActionsResponse getAllActionsOfVolume(long id) {
        return restTemplate.exchange(
                API_URL + "/volumes/" + id + "/actions",
                HttpMethod.GET,
                httpEntity,
                ActionsResponse.class).getBody();
    }

    /**
     * Get a specific action of a volume.
     *
     * @param id       ID of the volume
     * @param actionID ID of the action
     * @return Action object
     */
    public ActionResponse getActionOfVolume(long id, long actionID) {
        return restTemplate.exchange(
                API_URL + "/volumes/" + id + "/actions/" + actionID,
                HttpMethod.GET,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Attach a volume to a server.
     *
     * @param id                  ID of the volume
     * @param attachVolumeRequest Request object
     * @return Action object
     */
    public ActionResponse attachVolumeToServer(long id, AttachVolumeRequest attachVolumeRequest) {
        if (attachVolumeRequest.getServerID() == null)
            throw new InvalidParametersException("Server id must be specified");
        return restTemplate.postForEntity(
                API_URL + "/volumes/" + id + "/actions/attach",
                new HttpEntity<>(attachVolumeRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Detach a volume from a server.
     *
     * @param id ID of the volume
     * @return Action object
     */
    public ActionResponse detachVolume(long id) {
        return restTemplate.postForEntity(
                API_URL + "/volumes/" + id + "/actions/detach",
                new HttpEntity<>(httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Resize a volume.
     * Downsizing not possible!
     *
     * @param id                  ID of the volume
     * @param resizeVolumeRequest Request object
     * @return Action object
     */
    public ActionResponse resizeVolume(long id, ResizeVolumeRequest resizeVolumeRequest) {
        if (resizeVolumeRequest.getSize() < 10 || resizeVolumeRequest.getSize() > 10240)
            throw new InvalidParametersException("Size have to be between 10 and 10240");
        if (getVolume(id).getVolume().getSize() <= resizeVolumeRequest.getSize())
            throw new InvalidParametersException("Size must be greater than " + resizeVolumeRequest.getSize());
        return restTemplate.postForEntity(
                API_URL + "/volumes/" + id + "/actions/resize",
                new HttpEntity<>(resizeVolumeRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Change the protection mode of the volume.
     * You can only use delete, no rebuild!
     *
     * @param id                      ID of the volume
     * @param changeProtectionRequest Request object
     * @return Action object
     */
    public ActionResponse changeVolumeProtection(long id, ChangeProtectionRequest changeProtectionRequest) {
        if (changeProtectionRequest.isRebuild())
            throw new InvalidParametersException("Rebuild can't be used on volumes.");
        return restTemplate.postForEntity(
                API_URL + "/volumes/" + id + "/actions/change_protection",
                new HttpEntity<>(changeProtectionRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Get all networks which are in project.
     *
     * @return Response from API
     */
    public NetworksResponse getAllNetworks() {
        return restTemplate.exchange(
                API_URL + "/networks",
                HttpMethod.GET,
                httpEntity,
                NetworksResponse.class).getBody();
    }

    /**
     * Get all networks with specific name.
     *
     * @param name Name of the network
     * @return Response from API
     */
    public NetworksResponse getNetworksByName(String name) {
        return restTemplate.exchange(
                API_URL + "/networks?name=" + name,
                HttpMethod.GET,
                httpEntity,
                NetworksResponse.class).getBody();
    }

    /**
     * Get all networks with specific label.
     *
     * @param label Label which is attached to network
     * @return Response from API
     */
    public NetworksResponse getNetworksByLabel(String label) {
        return restTemplate.exchange(
                API_URL + "/networks?label_selector=" + label,
                HttpMethod.GET,
                httpEntity,
                NetworksResponse.class).getBody();
    }

    /**
     * Get a network with it's specific ID.
     *
     * @param id ID of the network
     * @return Response from API
     */
    public NetworkResponse getNetwork(long id) {
        return restTemplate.exchange(
                API_URL + "/networks/" + id,
                HttpMethod.GET,
                httpEntity,
                NetworkResponse.class).getBody();
    }

    /**
     * Update the labels or the name of a network.
     *
     * @param id                   ID of the network
     * @param updateNetworkRequest Request object
     * @return Response from API
     */
    public NetworkResponse updateNetwork(long id, UpdateNetworkRequest updateNetworkRequest) {
        return restTemplate.exchange(
                API_URL + "/networks/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateNetworkRequest, httpHeaders),
                NetworkResponse.class).getBody();
    }


    /**
     * Create a new network in your project.
     *
     * @param networkRequest Request object
     * @return Response from API
     */
    public NetworkResponse createNetwork(NetworkRequest networkRequest) {
        if (networkRequest.getIpRange() == null) throw new InvalidParametersException("IP-Range missing!");
        if (networkRequest.getName() == null) throw new InvalidParametersException("Name missing!");
        return restTemplate.postForEntity(
                API_URL + "/networks",
                new HttpEntity<>(networkRequest, httpHeaders),
                NetworkResponse.class).getBody();
    }

    /**
     * Delete a network.
     *
     * @param id ID of the network
     * @return There is no response.
     */
    public String deleteNetwork(long id) {
        return restTemplate.exchange(
                API_URL + "/networks/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Attaches a server to a private network.
     *
     * @param id                           ID of the server
     * @param attachServerToNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse attachServerToNetwork(long id, AttachServerToNetworkRequest attachServerToNetworkRequest) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/attach_to_network",
                new HttpEntity<>(attachServerToNetworkRequest, httpHeaders),
                ActionResponse.class).getBody();
    }


    /**
     * Detaches a server from a private network.
     *
     * @param id                             ID of the server
     * @param detachServerFromNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse detachServerFromNetwork(long id, DetachServerFromNetworkRequest detachServerFromNetworkRequest) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/detach_from_network",
                new HttpEntity<>(detachServerFromNetworkRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Change alias IPs of a network.
     *
     * @param id                             ID of the server
     * @param changeAliasIPsofNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse changeAliasIPsofNetwork(long id, ChangeAliasIPsofNetworkRequest changeAliasIPsofNetworkRequest) {
        return restTemplate.postForEntity(
                API_URL + "/servers/" + id + "/actions/change_alias_ips",
                new HttpEntity<>(changeAliasIPsofNetworkRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Change the protection configuration for a network
     *
     * @param id               ID of the network
     * @param changeProtection Request Object
     * @return Response from API
     */
    public ActionResponse changeNetworkProtection(long id, ChangeProtectionRequest changeProtection) {
        if (changeProtection.isRebuild())
            throw new InvalidParametersException("Rebuild not allowed in Network protection!");
        return restTemplate.postForEntity(
                API_URL + "/networks/" + id + "/actions/change_protection",
                new HttpEntity<>(changeProtection, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Get all performed Actions for a network
     *
     * @param id ID of the network
     * @return Response from API
     */
    public ActionsResponse getActionsForNetwork(long id) {
        return restTemplate.exchange(
                API_URL + "/networks/" + id + "/actions",
                HttpMethod.GET,
                httpEntity,
                ActionsResponse.class).getBody();
    }

    /**
     * Get an action for a network
     *
     * @param serverID ID of the network
     * @param actionID ID of the Action
     * @return Response from API
     */
    public ActionResponse getActionForNetwork(long serverID, long actionID) {
        return restTemplate.exchange(
                API_URL + "/networks/" + serverID + "/actions/" + actionID,
                HttpMethod.GET,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Add a new subnet to a network.
     *
     * @param id                        ID of the network
     * @param addSubnetToNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse addSubnetToNetwork(long id, AddSubnetToNetworkRequest addSubnetToNetworkRequest) {
        if (addSubnetToNetworkRequest.getNetworkZone() == null)
            throw new InvalidParametersException("Network zone required!");
        if (addSubnetToNetworkRequest.getType() == null) throw new InvalidParametersException("Type required!");
        return restTemplate.postForEntity(
                API_URL + "/networks/" + id + "/actions/add_subnet",
                new HttpEntity<>(addSubnetToNetworkRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Delete a subnet from a network.
     *
     * @param id                      ID of the network
     * @param deleteSubnetFromNetwork Request object
     * @return Response from API
     */
    public ActionResponse deleteSubnetFromNetwork(long id, DeleteSubnetFromNetwork deleteSubnetFromNetwork) {
        if (deleteSubnetFromNetwork.getIpRange() == null) throw new InvalidParametersException("Subnet missing");
        return restTemplate.postForEntity(
                API_URL + "/networks/" + id + "/actions/delete_subnet",
                new HttpEntity<>(deleteSubnetFromNetwork, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Add a route to a network.
     *
     * @param id             ID of the network
     * @param routeToNetwork Request object
     * @return Response from API
     */
    public ActionResponse addRouteToNetwork(long id, RouteToNetwork routeToNetwork) {
        if (routeToNetwork.getDestination() == null) throw new InvalidParametersException("Destination missing!");
        if (routeToNetwork.getGateway() == null) throw new InvalidParametersException("Gateway missing!");
        if (routeToNetwork.getGateway().equals("172.31.1.1"))
            throw new InvalidParametersException("Gateway cannot be 172.31.1.1!");
        return restTemplate.postForEntity(
                API_URL + "/networks/" + id + "/actions/add_route",
                new HttpEntity<>(routeToNetwork, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Delete a route from a network.
     *
     * @param id             ID of the network
     * @param routeToNetwork Request object
     * @return Response from API
     */
    public ActionResponse deleteRouteFromNetwork(long id, RouteToNetwork routeToNetwork) {
        if (routeToNetwork.getDestination() == null) throw new InvalidParametersException("Destination missing!");
        if (routeToNetwork.getGateway() == null) throw new InvalidParametersException("Gateway missing!");
        if (routeToNetwork.getGateway().equals("172.31.1.1"))
            throw new InvalidParametersException("Gateway cannot be 172.31.1.1!");
        return restTemplate.postForEntity(
                API_URL + "/networks/" + id + "/actions/delete_route",
                new HttpEntity<>(routeToNetwork, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Change the IP range of a network.
     * Shrinking not possible!
     *
     * @param id                     ID of the network
     * @param changeIPRangeOfNetwork Request object
     * @return Response from API
     */
    public ActionResponse changeIPRangeOfNetwork(long id, ChangeIPRangeOfNetwork changeIPRangeOfNetwork) {
        return restTemplate.postForEntity(
                API_URL + "/networks/" + id + "/actions/change_ip_range",
                new HttpEntity<>(changeIPRangeOfNetwork, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Get all certificates from the project.
     *
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates() {
        return restTemplate.exchange(
                API_URL + "/certificates",
                HttpMethod.GET, httpEntity,
                CertificatesResponse.class).getBody();
    }

    /**
     * Get a specific certificate by id.
     *
     * @param id ID of the certificate
     * @return CertificateResponse
     */
    public CertificateResponse getCertificate(long id) {
        return restTemplate.exchange(
                API_URL + "/certificates/" + id,
                HttpMethod.GET, httpEntity,
                CertificateResponse.class).getBody();
    }

    /**
     * Create a new certificate.
     *
     * @param createCertificateRequest CertificateRequest object with name, public- and private-key
     * @return CertificateResponse
     */
    public CertificateResponse createCertificate(CreateCertificateRequest createCertificateRequest) {
        return restTemplate.postForEntity(
                API_URL + "/certificates",
                new HttpEntity<>(createCertificateRequest, httpHeaders),
                CertificateResponse.class).getBody();
    }

    /**
     * Update a certificate.
     *
     * Available options to update:
     * - Name
     * - Labels
     *
     * @param id                       ID of the certificate
     * @param updateCertificateRequest Certificate Update object
     * @return CertificateResponse
     */
    public CertificateResponse updateCertificate(long id, UpdateCertificateRequest updateCertificateRequest) {
        return restTemplate.exchange(
                API_URL + "/certificates/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateCertificateRequest, httpHeaders),
                CertificateResponse.class).getBody();
    }

    /**
     * Retry an issuance or renewal for a managed certificate.
     * 
     * This method is only applicable to managed certificate where either the issuance
     * or renewal status is failed.
     * 
     * @param id ID of the certificate
     * @return ActionResponse
     */
    public ActionResponse retryCertificate(long id) {
        return restTemplate.exchange(
            API_URL + "/certificates/" + id + "/actions/retry",
            HttpMethod.POST,
            httpEntity,
            ActionResponse.class).getBody();
    }

    /**
     * Delete a certificate.
     *
     * @param id ID of the certificate
     * @return returns nothing...
     */
    public String deleteCertificate(long id) {
        return restTemplate.exchange(
                API_URL + "/certificates/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Get all Load Balancers.
     *
     * @return returns LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancers() {
        return restTemplate.exchange(
                API_URL + "/load_balancers",
                HttpMethod.GET,
                httpEntity,
                LoadBalancersResponse.class).getBody();
    }

    /**
     * Get a specific Load Balancer.
     *
     * @param id    ID of the Load Balancer
     * @return      returns LoadBalancerResponse
     */
    public LoadBalancerResponse getLoadBalancer(long id) {
        return restTemplate.exchange(
                API_URL + "/load_balancers/" + id,
                HttpMethod.GET,
                httpEntity,
                LoadBalancerResponse.class).getBody();
    }

    /**
     * Create a new Load Balancer.
     *
     * @param loadBalancerRequest   Load Balancer Request object
     * @return                      returns LoadBalancerResponse
     */
    public LoadBalancerResponse createLoadBalancer(LoadBalancerRequest loadBalancerRequest) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers",
                new HttpEntity<>(loadBalancerRequest, httpHeaders),
                LoadBalancerResponse.class).getBody();
    }

    /**
     * Update an existing Load Balancer.
     *
     * @param id                            ID of the Load Balancer
     * @param updateLoadBalancerRequest     Load Balancer Update Request Object
     * @return                              returns LoadBalancerResponse
     */
    public LoadBalancerResponse updateLoadBalancer(long id, UpdateLoadBalancerRequest updateLoadBalancerRequest) {
        return restTemplate.exchange(
                API_URL + "/load_balancers/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateLoadBalancerRequest, httpHeaders),
                LoadBalancerResponse.class).getBody();
    }

    /**
     * Delete a Load Balancer.
     *
     * @param id    ID of the Load Balancer
     * @return      returns nothing
     */
    public String deleteLoadBalancer(long id) {
        return restTemplate.exchange(
                API_URL + "/load_balancers/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
    }

    /**
     * Get all actions of a Load Balancer.
     *
     * @param id    ID of the Load Balancer
     * @return      returns ActionsResponse
     */
    public ActionsResponse getAllActionsOfLoadBalancer(long id) {
        return restTemplate.exchange(
                API_URL + "/load_balancers/" + id + "/actions",
                HttpMethod.GET,
                httpEntity,
                ActionsResponse.class).getBody();
    }

    /**
     * Get an action of a Load Balancer.
     *
     * @param id            ID of the Load Balancer
     * @param actionId      Action ID
     * @return              returns ActionResponse
     */
    public ActionResponse getActionOfLoadBalancer(long id, long actionId) {
        return restTemplate.exchange(
                API_URL + "/load_balancers/" + id + "/actions/" + actionId,
                HttpMethod.GET,
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Add a service to a Load Balancer.
     *
     * @param id                    ID of the Load Balancer
     * @param lbServiceRequest      Load Balancer Service Request
     * @return                      returns LoadBalancerResponse
     */
    public LoadBalancerResponse addServiceToLoadBalancer(long id, LBServiceRequest lbServiceRequest) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/add_service",
                new HttpEntity<>(lbServiceRequest, httpHeaders),
                LoadBalancerResponse.class).getBody();
    }

    /**
     * Update a service of a Load Balancer.
     *
     * @param id                    ID of the Load Balancer
     * @param lbServiceRequest      Load Balancer Service Request
     * @return                      returns LoadBalancerResponse
     */
    public LoadBalancerResponse updateServiceOfLoadBalancer(long id, LBServiceRequest lbServiceRequest) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/update_service",
                new HttpEntity<>(lbServiceRequest, httpHeaders),
                LoadBalancerResponse.class).getBody();
    }

    /**
     * Delete a service of a Load Balancer.
     *
     * @param id            ID of the Load Balancer
     * @param listenPort    The desired "listen port" of the service
     * @return              returns ActionResponse
     */
   public ActionResponse deleteServiceOfLoadBalancer(long id, long listenPort) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/delete_service",
                new HttpEntity<>("{ \"listen_port\": " + listenPort + "}", httpHeaders),
                ActionResponse.class).getBody();
   }

    /**
     * Add an target to a Load Balancer.
     *
     * @param id                ID of the Load Balancer
     * @param lbTargetRequest   Load Balancer Target Request
     * @return                  returns ActionResponse
     */
   public ActionResponse addTargetToLoadBalancer(long id, LBTargetRequest lbTargetRequest) {
       return restTemplate.postForEntity(
               API_URL + "/load_balancers/" + id + "/actions/add_target",
               new HttpEntity<>(lbTargetRequest, httpHeaders),
               ActionResponse.class).getBody();
   }

    /**
     * Removes an target from a load balancer.
     *
     * @param id                ID of the Load Balancer
     * @param lbTargetRequest   Load Balancer Target Request
     * @return                  returns ActionResponse
     */
    public ActionResponse removeTargetFromLoadBalancer(long id, LBTargetRequest lbTargetRequest) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/remove_target",
                new HttpEntity<>(lbTargetRequest, httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Changes the algorithm that determines to which target new requests are sent.
     *
     * @param id            ID of the Load Balancer
     * @param algorithmType Algorithm Type
     * @return              returns ActionResponse
     */
    public ActionResponse changeAlgorithmOfLoadBalancer(long id, String algorithmType) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/change_algorithm",
                new HttpEntity<>("{\"type\": \"" + algorithmType + "\"}", httpHeaders),
                ActionResponse.class).getBody();
    }


    /**
     * Changes the type of a Load Balancer.
     *
     * @param id                ID of the Load Balancer
     * @param loadBalancerType  New type of the Load Balancer
     * @return                  returns ActionResponse
     */
    public ActionResponse changeTypeOfLoadBalancer(long id, String loadBalancerType) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/change_type",
                new HttpEntity<>("{\"load_balancer_type\": \"" + loadBalancerType + "\"}", httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Attach a network to a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkID ID of the Network
     * @param ip        IP for the Load Balancer in this private network
     * @return          returns ActionResponse
     */
    public ActionResponse attachNetworkToLoadBalancer(long id, long networkID, String ip) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/attach_to_network",
                new HttpEntity<>("{\"network\": \"" + networkID + "\", \"ip\": \""+ ip +"\"}", httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Attach a network to a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkID ID of the Network
     * @return          returns ActionResponse
     */
    public ActionResponse attachNetworkToLoadBalancer(long id, long networkID) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/attach_to_network",
                new HttpEntity<>("{\"network\": \"" + networkID + "\"}", httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Detach a network from a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkID ID of the Network
     * @return          returns ActionResponse
     */
    public ActionResponse detachNetworkFromLoadBalancer(long id, long networkID) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/detach_from_network",
                new HttpEntity<>("{\"network\": \"" + networkID + "\"}", httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Enable the public interface of a Load Balancer.
     *
     * @param id    ID of the Load Balancer
     * @return      returns ActionResponse
     */
    public ActionResponse enablePublicInterfaceOfLoadBalancer(long id) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/enable_public_interface",
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Disable the public interface of a Load Balancer.
     *
     * @param id    ID of the Load Balancer
     * @return      returns ActionResponse
     */
    public ActionResponse disablePublicInterfaceOfLoadBalancer(long id) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/disable_public_interface",
                httpEntity,
                ActionResponse.class).getBody();
    }

    /**
     * Change the protection configuration of a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param delete    Delete protection
     * @return          returns ActionResponse
     */
    public ActionResponse changeProtectionOfLoadBalancer(long id, boolean delete) {
        return restTemplate.postForEntity(
                API_URL + "/load_balancers/" + id + "/actions/change_protection",
                new HttpEntity<>("{\"delete\": " + delete + "}", httpHeaders),
                ActionResponse.class).getBody();
    }

    /**
     * Get a specific placement group.
     *
     * @param id placement group ID
     * @return PlacementGroupResponse
     */
    public PlacementGroupResponse getPlacementGroup(long id) {
        return restTemplate.exchange(
                API_URL + "/placement_groups/" + id,
                HttpMethod.GET,
                httpEntity,
                PlacementGroupResponse.class).getBody();
    }

    /**
     * Get all placement groups.
     *
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroups() {
        return restTemplate.exchange(
                API_URL + "/placement_groups",
                HttpMethod.GET,
                httpEntity,
                PlacementGroupsResponse.class).getBody();
    }

    /**
     * Get placement group by name.
     *
     * @param name name of the placement grouo
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroupByName(String name) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL + "/placement_groups")
                .queryParam("name", name);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                PlacementGroupsResponse.class).getBody();
    }

    /**
     * Get placement groups by label selector.
     *
     * @param labelSelector label selector used by resource
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroupByLabelSelector(String labelSelector) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL + "/placement_groups")
                .queryParam("label_selector", labelSelector);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                PlacementGroupsResponse.class).getBody();
    }

    /**
     * Get placement groups by type
     *
     * @param type Type of the placement group
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroupByLabelSelector(PlacementGroupType type) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL + "/placement_groups")
                .queryParam("type", type);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                PlacementGroupsResponse.class).getBody();
    }

    /**
     * Create a placement group.
     *
     * @param placementGroupRequest PlacementGroupRequest object
     * @return PlacementGroupResponse
     */
    public PlacementGroupResponse createPlacementGroup(PlacementGroupRequest placementGroupRequest) {
        return restTemplate.postForEntity(
                API_URL + "/placement_groups",
                new HttpEntity<>(placementGroupRequest, httpHeaders),
                PlacementGroupResponse.class).getBody();
    }

    /**
     * Delete a placement group.
     *
     * @param id placement group ID
     * @return ActionResponse
     */
    public String deletePlacementGroup(long id) {
        return restTemplate.exchange(
                API_URL + "/placement_groups/" + id,
                HttpMethod.DELETE,
                httpEntity,
                String.class).getBody();
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