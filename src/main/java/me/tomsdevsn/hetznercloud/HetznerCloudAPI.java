package me.tomsdevsn.hetznercloud;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.tomsdevsn.hetznercloud.exception.InvalidParametersException;
import me.tomsdevsn.hetznercloud.objects.general.PlacementGroupType;
import me.tomsdevsn.hetznercloud.objects.pagination.PaginationParameters;
import me.tomsdevsn.hetznercloud.objects.request.*;
import me.tomsdevsn.hetznercloud.objects.response.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class HetznerCloudAPI {

    private static final String API_URL = "https://api.hetzner.cloud/v1";

    private final OkHttpClient client;

    private final String hcloudToken;
    private final ObjectMapper objectMapper;

    /**
     * Initial method to use the API
     *
     * @param hcloudToken API-Token for Hetzner Cloud API
     *              The API token can be created within the Hetzner Cloud Console
     */
    public HetznerCloudAPI(String hcloudToken) {

        if (hcloudToken == null || hcloudToken.isBlank()) {
            throw new RuntimeException("no Hetzner cloud token provided");
        }

        this.hcloudToken = hcloudToken;

        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Get all actions in a project.
     *
     * @return All Actions without pagination
     */
    public ActionsResponse getActions() {
        return getActions(new PaginationParameters(null, null));
    }

    /**
     * Get all actions in a project.
     *
     * @param paginationParameters Pagination parameters
     * @return ActionsResponse
     */
    public ActionsResponse getActions(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/actions")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ActionsResponse.class);
    }

    /**
     * Get details about an action by id.
     *
     * @param id ID of the action
     * @return ActionResponse
     */
    public ActionResponse getAction(long id) {
        return get(
                API_URL + "/actions/" + id,
                ActionResponse.class);
    }

    /**
     * Get all servers in a project.
     *
     * @return All servers as Servers object without pagination
     */
    public Servers getServers() {
        return getServers(new PaginationParameters(null, null));
    }

    /**
     * Get all servers in a project.
     *
     * @param paginationParameters Pagination parameters
     * @return All servers as Servers object
     */
    public Servers getServers(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/servers")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                Servers.class);
    }

    /**
     * Get servers by name.
     *
     * @param name Name of the server
     * @return Matching servers as Servers object without pagination
     */
    public Servers getServerByName(String name) {
        return getServerByName(name, new PaginationParameters(null, null));
    }

    /**
     * Get servers by name.
     *
     * @param name                 Name of the server
     * @param paginationParameters Pagination parameters
     * @return Matching servers as Servers object
     */
    public Servers getServerByName(String name, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/servers")
                        .queryParam("name", name)
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                Servers.class);
    }

    /**
     * Get a server by id
     *
     * @param id id of the server
     * @return The server with the specific ID
     * @deprecated This method is deprecated and will be removed with a further release. Please use {@link #getServer(long id)}
     */
    @Deprecated
    public GetServerResponse getServerById(long id) {
        return get(
                API_URL + "/servers/" + id,
                GetServerResponse.class);
    }

    /**
     * Get a server by id
     *
     * @param id id of the server
     * @return GetServerResponse
     */
    public GetServerResponse getServer(long id) {
        return get(
                UrlBuilder.from(API_URL + "/servers")
                        .queryParam("id", id)
                        .toUri(),
                GetServerResponse.class);
    }

    /**
     * Create a server.
     *
     * @param serverRequest Parameters for server creation.
     * @return ServerResponse including Action status, Server object and (if no ssh key defined) root password.
     */
    public ServerResponse createServer(ServerRequest serverRequest) {
        serverRequest.setServerType(serverRequest.getServerType().toLowerCase());   // Case-sensitive fix
        return post(
                API_URL + "/servers",
                serverRequest,
                ServerResponse.class);
    }

    /**
     * Delete a server
     *
     * @param id id of the server.
     * @return ActionResponse object
     */
    public ActionResponse deleteServer(long id) {
        return delete(
                API_URL + "/servers/" + id,
                ActionResponse.class);
    }

    /**
     * Change the name of the server.
     *
     * @param id            id of the server.
     * @param newServerName request
     * @return ServernameChangeResponse object
     */
    public ServernameChangeResponse changeServerName(long id, ServernameChangeRequest newServerName) {
        return put(
                API_URL + "/servers/" + id,
                newServerName,
                ServernameChangeResponse.class);
    }

    /**
     * Request a WebSocket URL for a server.
     *
     * @param id id of the server
     * @return ConsoleResponse object
     */
    public ConsoleResponse requestConsole(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/request_console",
                ConsoleResponse.class);
    }

    /**
     * Change the protection configuration of a server.
     *
     * @param id               id of the server
     * @param changeProtection Request Object (both optional)
     * @return ActionResponse object
     */
    public ActionResponse changeServerProtection(long id, ChangeProtectionRequest changeProtection) {
        return post(
                API_URL + "/servers/" + id + "/actions/change_protection",
                changeProtection,
                ActionResponse.class);
    }

    /**
     * Add a server to a placement group.
     * Server has to be stopped.
     *
     * @param serverId         server id
     * @param placementGroupId placement group id
     * @return ActionResponse
     */
    public ActionResponse addServerToPlacementGroup(long serverId, long placementGroupId) {
        return post(
                API_URL + "/servers/" + serverId + "/actions/add_to_placement_group",
                new PlacementGroupAddServerRequest(placementGroupId),
                ActionResponse.class);
    }

    /**
     * Remove a server from a placement group.
     *
     * @param serverId server id
     * @return ActionResponse
     */
    public ActionResponse removeServerFromPlacementGroup(long serverId) {
        return post(
                API_URL + "/servers/" + serverId + "/actions/remove_from_placement_group",
                ActionResponse.class);
    }

    /**
     * Get all performed Actions of a server.
     *
     * @param id id of the server
     * @return ActionsResponse object
     */
    public ActionsResponse getAllActionsOfServer(long id) {
        return get(
                API_URL + "/servers/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Get an action for a server
     *
     * @param serverID ID of the Server
     * @param actionID ID of the Action
     * @return ActionResponse object
     */
    public ActionResponse getActionOfServer(long serverID, long actionID) {
        return get(
                API_URL + "/servers/" + serverID + "/actions/" + actionID,
                ActionResponse.class);
    }

    /**
     * Get all performed Actions of a Floating IP
     *
     * @param id ID of the FloatingIP
     * @return ActionsResponse object
     */
    public ActionsResponse getActionsOfFloatingIP(long id) {
        return get(API_URL + "/floating_ips/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Get an action for a server
     *
     * @param floatingIPID ID of the Floating IP
     * @param actionID     ID of the Action
     * @return ActionResponse object
     */
    public ActionResponse getActionOfFloatingIP(long floatingIPID, long actionID) {
        return get(
                API_URL + "/floating_ips/" + floatingIPID + "/actions/" + actionID,
                ActionResponse.class);
    }

    /**
     * Power on a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse powerOnServer(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/poweron",
                ActionResponse.class);
    }

    /**
     * Force power off a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse powerOffServer(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/poweroff",
                ActionResponse.class);
    }

    /**
     * Reboot a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse softRebootServer(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/reboot",
                ActionResponse.class);
    }

    /**
     * Reset a specific server with the id
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse resetServer(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/reset",
                ActionResponse.class);
    }

    /**
     * Soft-shutdown a specific server with the id
     *
     * @param id ID of the server
     * @return respond
     */
    public ActionResponse shutdownServer(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/shutdown",
                ActionResponse.class);
    }

    /**
     * Resets the root password from a specific server with the id
     *
     * @param id ID of the server
     * @return respond
     */
    public ResetRootPasswordResponse resetRootPassword(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/reset_password",
                ResetRootPasswordResponse.class);
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id ID of the server
     * @return respond
     */
    public EnableRescueResponse enableRescue(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                EnableRescueResponse.class);
    }

    /**
     * Enables the rescue mode from the server
     *
     * @param id                  ID of the server
     * @param enableRescueRequest Request object
     * @return respond
     */
    public EnableRescueResponse enableRescue(long id, EnableRescueRequest enableRescueRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                enableRescueRequest,
                EnableRescueResponse.class);
    }

    /**
     * Enables the rescue mode from the server and reset the server
     *
     * @param id ID of the server
     * @return respond
     */
    public EnableRescueResponse enableRescueAndReset(long id) {
        EnableRescueResponse request = post(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                EnableRescueResponse.class);
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
        EnableRescueResponse request = post(
                API_URL + "/servers/" + id + "/actions/enable_rescue",
                enableRescueRequest,
                EnableRescueResponse.class);
        resetServer(id);
        return request;
    }

    /**
     * Disables the rescue mode from the server.
     * <p>
     * Only needed, if the server doesn't booted into the rescue mode.
     *
     * @param id ID of the server
     * @return respond
     */
    public ActionResponse disableRescue(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/disable_rescue",
                ActionResponse.class);
    }

    /**
     * Rebuild a server, with the specific image.
     * <p>
     * example: ubuntu-16.04
     *
     * @param id                   ID of the server
     * @param rebuildServerRequest Request object
     * @return respond
     */
    public RebuildServerResponse rebuildServer(long id, RebuildServerRequest rebuildServerRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/rebuild",
                rebuildServerRequest,
                RebuildServerResponse.class);
    }

    /**
     * Change the type from the server
     * <p>
     * example: cx11 to cx21
     * <p>
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
        return post(
                API_URL + "/servers/" + id + "/actions/change_type",
                changeTypeRequest,
                ActionResponse.class);
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
        return get(
                API_URL + "/servers/" + id + "/metrics?type=" + metricType + "&start=" + start + "&end=" + end,
                MetricsResponse.class);
    }

    /**
     * Create a image from a server
     *
     * @param id                 ID of the server
     * @param createImageRequest Request object
     * @return respond
     */
    public CreateImageResponse createImage(long id, CreateImageRequest createImageRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/create_image",
                createImageRequest,
                CreateImageResponse.class);
    }


    /**
     * Enable or disable the Protection of an Image
     *
     * @param id                ID of the image
     * @param protectionRequest Only the delete parameter!
     * @return ActionResponse object
     */
    public ActionResponse changeImageProtection(long id, ChangeProtectionRequest protectionRequest) {
        return post(
                API_URL + "/images/" + id + "/actions/change_protection",
                protectionRequest,
                ActionResponse.class);
    }

    /**
     * @param id                  ID of the server
     * @param enableBackupRequest Request object
     * @return respone
     * @deprecated due Hetzner changed Request header
     * <p>
     * Enable the backups from a server
     * <p>
     * Please reminder, that will increase the price of the server by 20%
     */
    @Deprecated
    public EnableBackupResponse enableBackup(long id, EnableBackupRequest enableBackupRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/enable_backup",
                enableBackupRequest,
                EnableBackupResponse.class);
    }

    /**
     * Enable the backups from a server
     * <p>
     * Please reminder, that will increase the price of the server by 20%
     *
     * @param id ID of the server
     * @return respone
     */
    public ActionResponse enableBackup(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/enable_backup",
                ActionResponse.class);
    }

    /**
     * Disable the backups from a server
     * <p>
     * Caution!: This will delete all existing backups immediately
     *
     * @param id ID of the server
     * @return respond
     */
    public DisableBackupResponse disableBackup(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/disable_backup",
                DisableBackupResponse.class);
    }

    /**
     * Get all available ISOs.
     *
     * @return ISOSResponse
     */
    public ISOSResponse getISOS() {
        return getISOS(new PaginationParameters(null, null));
    }

    /**
     * Get all available ISO's.
     *
     * @param paginationParameters Pagination parametres
     * @return ISOSResponse
     */
    public ISOSResponse getISOS(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/isos")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ISOSResponse.class);
    }

    /**
     * Get an ISO by ID
     *
     * @param id ID of the ISO
     * @return ISOResponse Object
     */
    public ISOResponse getISOById(long id) {
        return get(
                API_URL + "/isos/" + id,
                ISOResponse.class);
    }

    /**
     * Attach an ISO to a server.
     * <p>
     * To get all ISO's {@link #getISOS}
     *
     * @param id               of the server
     * @param attachISORequest Request object
     * @return ActionResponse object
     */
    public ActionResponse attachISO(long id, AttachISORequest attachISORequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/attach_iso",
                attachISORequest,
                ActionResponse.class);
    }

    /**
     * Detach an ISO from a server.
     *
     * @param id of the server
     * @return respond
     */
    public ActionResponse detachISO(long id) {
        return post(
                API_URL + "/servers/" + id + "/actions/detach_iso",
                ActionResponse.class);
    }

    /**
     * Changes the reverse DNS entry from a server.
     * <p>
     * Floating IPs assigned to the server are not affected!
     *
     * @param id                      ID of the server
     * @param changeReverseDNSRequest Request object
     * @return respond
     */
    public ActionResponse changeDNSPTR(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/change_dns_ptr",
                changeReverseDNSRequest,
                ActionResponse.class);
    }

    /**
     * Get a Datacenter by ID
     *
     * @param id of the Datacenter
     * @return respond
     */
    public DatacenterResponse getDatacenter(long id) {
        return get(
                API_URL + "/datacenters/" + id,
                DatacenterResponse.class);
    }

    /**
     * Get all available datacenters and the recommendation
     *
     * @return respond
     */
    public DatacentersResponse getDatacenters() {
        return get(
                API_URL + "/datacenters",
                DatacentersResponse.class);
    }

    /**
     * Get a datacenter by name
     *
     * @param name of the datacenter
     * @return DatacentersResponse
     */
    public DatacentersResponse getDatacenter(String name) {
        return get(
                API_URL + "/datacenters?" + name,
                DatacentersResponse.class);
    }

    /**
     * Get all prices from the products
     *
     * @return PricingResponse
     */
    public PricingResponse getPricing() {
        return get(
                API_URL + "/pricing",
                PricingResponse.class);
    }

    /**
     * Get all Primary IPs in a project
     *
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPs() {
        return getPrimaryIPs(null, new PaginationParameters(null, null));
    }

    /**
     * Get all Primary IPs in a project by label selector
     *
     * @param labelSelector Label selector
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPs(String labelSelector) {
        return getPrimaryIPs(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Primary IPs in a project
     *
     * @param labelSelector Label selector
     * @param paginationParameters Pagination parametres
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPs(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/primary_ips")
                        .queryParamIfPresent("label_selector", Optional.ofNullable(labelSelector))
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                PrimaryIPsResponse.class);
    }

    /**
     * Get a Primary IP by its name in a project
     *
     * @param name Name of the Primary IP
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIPByName(String name) {
        return get(
                UrlBuilder.from(API_URL + "/primary_ips")
                        .queryParam("name", name)
                        .toUri(),
                PrimaryIPsResponse.class);
    }

    /**
     * Get a Primary IP by the IP address itself
     *
     * @param ip IP address
     * @return PrimaryIPsResponse
     */
    public PrimaryIPsResponse getPrimaryIP(String ip) {
        return get(
                UrlBuilder.from(API_URL + "/primary_ips")
                        .queryParam("ip", ip)
                        .toUri(),
                PrimaryIPsResponse.class);
    }

    /**
     * Get a Primary IP by its id
     *
     * @param id id of the Primary IP
     * @return PrimaryIPResponse
     */
    public PrimaryIPResponse getPrimaryIP(long id) {
        return get(
                API_URL + "/primary_ips/" + id,
                PrimaryIPResponse.class);
    }

    /**
     * Create a Primary IP
     *
     * @param createPrimaryIPRequest Primary IP request
     * @return CreatePrimaryIPResponse
     */
    public CreatePrimaryIPResponse createPrimaryIP(CreatePrimaryIPRequest createPrimaryIPRequest) {
        if (createPrimaryIPRequest.getAssigneeId() != null && createPrimaryIPRequest.getDatacenter() != null)
            throw new InvalidParametersException("Assignee id and datacenter can not be set at the same time");

        return post(
                API_URL + "/primary_ips",
                createPrimaryIPRequest,
                CreatePrimaryIPResponse.class);
    }

    /**
     * Update a Primary IP
     * @param updatePrimaryIPRequest Primary IP Update request
     * @return PrimaryIPResponse
     */
    public PrimaryIPResponse updatePrimaryIP(long id, UpdatePrimaryIPRequest updatePrimaryIPRequest) {
        return post(
                API_URL + "/primary_ips/" + id,
                updatePrimaryIPRequest,
                PrimaryIPResponse.class);
    }

    /**
     * Assign a Primary IP to a resource.
     *
     * @param id id of the Primary IP
     * @param assignPrimaryIPRequest Primary IP Resource Assignment request
     * @return ActionResponse
     */
    public ActionResponse assignPrimaryIP(long id, AssignPrimaryIPRequest assignPrimaryIPRequest) {
        return post(
                API_URL + "/primary_ips/" + id + "/actions/assign",
                assignPrimaryIPRequest,
                ActionResponse.class);
    }

    /**
     * Unassign a Primary IP from a resource.
     *
     * @param id id of the Primary IP
     * @return ActionResponse
     */
    public ActionResponse unassignPrimaryIP(long id) {
        return post(
                API_URL + "/primary_ips/" + id + "/actions/unassign",
                ActionResponse.class);
    }

    /**
     * Update a reverse DNS entry for a Primary IP
     *
     * @param id id of the Primary IP
     * @param changeReverseDNSRequest Reverse DNS update change
     * @return ActionResponse
     */
    public ActionResponse changePrimaryIPReverseDNS(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return post(
                API_URL + "/primary_ips/" + id + "/actions/change_dns_ptr",
                changeReverseDNSRequest,
                ActionResponse.class);
    }

    public ActionResponse changePrimaryIPProtection(long id, ChangeProtectionRequest changeProtectionRequest) {
        if (changeProtectionRequest.isRebuild())
            throw new InvalidParametersException("Only delete is valid for Primary IPs");

        return post(
                API_URL + "/primary_ips/" + id + "/actions/change_protection",
                changeProtectionRequest,
                ActionResponse.class);
    }

    /**
     * Delete a Primary IP
     *
     * @param id id of the Primary IP
     * @return nothing
     */
    public String deletePrimaryIP(Long id) {
        return delete(
                API_URL + "/primary_ips/" + id,
                String.class);
    }

    /**
     * Get all Floating IPs in a project.
     *
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs() {
        return getFloatingIPs(new PaginationParameters(null, null));
    }

    /**
     * Get all Floating IPs in a project.
     *
     * @param paginationParameters Pagination parametres
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/floating_ips")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                FloatingIPsResponse.class);
    }

    /**
     * Get all Floating IPs in a project by label selector.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector Label selector
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs(String labelSelector) {
        return getFloatingIPs(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all Floating IPs in a project.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector        Label selector
     * @param paginationParameters Pagination parametres
     * @return FloatingIPsResponse
     */
    public FloatingIPsResponse getFloatingIPs(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/floating_ips")
                        .queryParam("label_selector", labelSelector)
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                FloatingIPsResponse.class);
    }

    /**
     * Get a specific Floating IP.
     *
     * @param id ID of the Floating IP
     * @return GetFloatingIPResponse
     */
    public GetFloatingIPResponse getFloatingIP(long id) {
        return get(
                API_URL + "/floating_ips/" + id,
                GetFloatingIPResponse.class);
    }

    /**
     * Create a Floating IP for the project or for a Server.
     *
     * @param floatingIPRequest Request object
     * @return FloatingIPResponse object
     */
    public FloatingIPResponse createFloatingIP(FloatingIPRequest floatingIPRequest) {
        return post(
                API_URL + "/floating_ips",
                floatingIPRequest,
                FloatingIPResponse.class);
    }

    /**
     * Enable or disable the Protection of a Floating IP
     *
     * @param id                ID of the Floating IP
     * @param protectionRequest Only the delete parameter!
     * @return ActionResponse object
     */
    public ActionResponse changeFloatingIPProtection(long id, ChangeProtectionRequest protectionRequest) {
        return post(
                API_URL + "/floating_ips/" + id + "/actions/change_protection",
                protectionRequest,
                ActionResponse.class);
    }

    /**
     * Change the description of a Floating IP.
     *
     * @param id                           ID of the Floating IP
     * @param descriptionFloatingIPRequest Request object
     * @return GetFloatingIPResponse object
     */
    public GetFloatingIPResponse changeDescriptionOfFloatingIP(long id, DescriptionFloatingIPRequest descriptionFloatingIPRequest) {
        return put(
                API_URL + "/floating_ips/" + id,
                descriptionFloatingIPRequest,
                GetFloatingIPResponse.class);
    }

    /**
     * Assign a Floating IP to a server
     *
     * @param id                      ID of the Floating IP
     * @param assignFloatingIPRequest Request object
     * @return ActionResponse object
     */
    public ActionResponse assignFloatingIP(long id, AssignFloatingIPRequest assignFloatingIPRequest) {
        return post(
                API_URL + "/floating_ips/" + id + "/actions/assign",
                assignFloatingIPRequest,
                ActionResponse.class);
    }

    /**
     * Unassign a Floating IP from a server
     *
     * @param id ID of the Floating IP
     * @return ActionResponse object
     */
    public ActionResponse unassignFloatingIP(long id) {
        return post(
                API_URL + "/floating_ips/" + id + "/actions/unassign",
                ActionResponse.class);
    }

    /**
     * Change the reverse DNS entry for a Floating IP
     *
     * @param id                      ID of the Floating IP
     * @param changeReverseDNSRequest Request object
     * @return ActionResponse object
     */
    public ActionResponse changeFloatingReverseDNS(long id, ChangeReverseDNSRequest changeReverseDNSRequest) {
        return post(
                API_URL + "/floating_ips/" + id + "/actions/change_dns_ptr",
                changeReverseDNSRequest,
                ActionResponse.class);
    }

    /**
     * Delete a Floating IP.
     * <p>
     * This object does not have a respond!
     *
     * @param id ID of the Floating ID
     * @return String
     */
    public String deleteFloatingIP(long id) {
        return delete(
                API_URL + "/floating_ips/" + id,
                String.class);
    }

    /**
     * Update the description or labels of a Floating IP.
     *
     * @param id                      ID of the Floating IP
     * @param updateFloatingIPRequest Request Object
     * @return Response from API (Action will be null)
     */
    public FloatingIPResponse updateFloatingIP(long id, UpdateFloatingIPRequest updateFloatingIPRequest) {
        return put(
                API_URL + "/floating_ips/" + id,
                updateFloatingIPRequest,
                FloatingIPResponse.class);
    }

    /**
     * Get all SSH keys.
     *
     * @return SSHKeysResponse
     */
    public SSHKeysResponse getSSHKeys() {
        return getSSHKeys(new PaginationParameters(null, null));
    }

    /**
     * Get all SSH keys.
     *
     * @param paginationParameters Pagination parameters
     * @return SSHKeysResponse
     */
    public SSHKeysResponse getSSHKeys(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/ssh_keys")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                SSHKeysResponse.class);
    }

    /**
     * Get an SSH key by ID.
     *
     * @param id ID of the SSH key
     * @return SSHKeyResponse
     */
    public SSHKeyResponse getSSHKey(long id) {
        return get(
                API_URL + "/ssh_keys/" + id,
                SSHKeyResponse.class);
    }

    /**
     * Get a SSH key by name.
     *
     * @param name name of the SSH key
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKeyByName(String name) {
        return get(
                UrlBuilder.from(API_URL + "/ssh_keys")
                        .queryParam("name", name)
                        .toUri(),
                SSHKeysResponse.class);
    }

    /**
     * Get a SSH key by the fingerprint.
     *
     * @param fingerprint Fingerprint of the SSH key
     * @return SSHKeysResponse object
     */
    public SSHKeysResponse getSSHKeyByFingerprint(String fingerprint) {
        return get(
                UrlBuilder.from(API_URL + "/ssh_keys")
                        .queryParam("fingerprint", fingerprint)
                        .toUri(),
                SSHKeysResponse.class);
    }

    /**
     * Create a SSH key.
     *
     * @param sshKeyRequest Request object
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse createSSHKey(SSHKeyRequest sshKeyRequest) {
        return post(
                API_URL + "/ssh_keys",
                sshKeyRequest,
                SSHKeyResponse.class);
    }

    /**
     * Change the name of a SSH key
     *
     * @param id                      ID of the SSH key
     * @param changeSSHKeyNameRequest Request object
     * @return SSHKeyResponse object
     * @deprecated use {@link #updateSSHKey(long, UpdateSSHKeyRequest)} instead
     */
    @Deprecated
    public SSHKeyResponse changeSSHKeyName(long id, ChangeSSHKeyNameRequest changeSSHKeyNameRequest) {
        return put(
                API_URL + "/ssh_keys/" + id,
                changeSSHKeyNameRequest,
                SSHKeyResponse.class);
    }

    /**
     * Update parameters of a SSH key
     *
     * @param id                  ID of the SSH key
     * @param updateSSHKeyRequest Request Object
     * @return SSHKeyResponse object
     */
    public SSHKeyResponse updateSSHKey(long id, UpdateSSHKeyRequest updateSSHKeyRequest) {
        return put(
                API_URL + "/ssh_keys/" + id,
                updateSSHKeyRequest,
                SSHKeyResponse.class);
    }

    /**
     * Delete a SSH key.
     * <p>
     * This object does not have a respond!
     *
     * @param id ID of the SSH key
     * @return String
     */
    public String deleteSSHKey(long id) {
        return delete(
                API_URL + "/ssh_keys/" + id,
                String.class);
    }

    /**
     * Get all Server types.
     *
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypes() {
        return get(
                API_URL + "/server_types",
                ServerTypesResponse.class);
    }


    /**
     * Get all Load Balancer types.
     *
     * @return LoadBalancerTypeResponse object
     * @deprecated Will be removed with a further release. Please use {@link #getLoadBalancerTypes()}
     */
    @Deprecated
    public LoadBalancerTypeResponse getAllLoadBalancerTypes() {
        return get(
                API_URL + "/load_balancer_types",
                LoadBalancerTypeResponse.class);
    }

    /**
     * Get all Load Balancer types.
     *
     * @return LoadBalancerTypesResponse object
     */
    public LoadBalancerTypesResponse getLoadBalancerTypes() {
        return get(
                API_URL + "/load_balancer_types",
                LoadBalancerTypesResponse.class);
    }

    /**
     * Get all Load Balancer types by name.
     *
     * @param name Name of the Load Balancer type.
     * @return LoadBalancerTypeResponse object
     * @deprecated Will be removed with a further release. Please use {@link #getLoadBalancerTypeByName(String name)}
     */
    @Deprecated
    public LoadBalancerTypeResponse getAllLoadBalancerTypesByName(String name) {
        return get(
                UrlBuilder.from(API_URL + "/load_balancer_types")
                        .queryParam("name", name)
                        .toUri(),
                LoadBalancerTypeResponse.class);
    }

    /**
     * Get Load Balancer type by name.
     *
     * @param name Name of the Load Balancer type
     * @return LoadBalancerTypesResponse object
     */
    public LoadBalancerTypesResponse getLoadBalancerTypeByName(String name) {
        return get(
                UrlBuilder.from(API_URL + "/load_balancer_types")
                        .queryParam("name", name)
                        .toUri(),
                LoadBalancerTypesResponse.class);
    }

    //TODO: Add #getLoadBalancerType method. For that, #getAllLoadBalancerTypes has to be removed.

    /**
     * Get a Server type by name.
     *
     * @param name name of the Server type
     * @return ServerTypesResponse object
     */
    public ServerTypesResponse getServerTypeByName(String name) {
        return get(
                UrlBuilder.from(API_URL + "/server_types")
                        .queryParam("name", name)
                        .toUri(),
                ServerTypesResponse.class);
    }

    /**
     * Get a Server type by id.
     *
     * @param id id of the Server type
     * @return ServerTypeResponse object
     */
    public ServerTypeResponse getServerType(long id) {
        return get(
                API_URL + "/server_types/" + id,
                ServerTypeResponse.class);
    }

    /**
     * Get all available Locations.
     *
     * @return LocationsResponse object
     */
    public LocationsResponse getLocations() {
        return get(
                API_URL + "/locations",
                LocationsResponse.class);
    }

    /**
     * Get a Location by name.
     *
     * @param name Name of the location
     * @return LocationsResponse object
     */
    public LocationsResponse getLocationByName(String name) {
        return get(
                UrlBuilder.from(API_URL + "/locations")
                        .queryParam("name", name)
                        .toUri(),
                LocationsResponse.class);
    }

    /**
     * Get a location by id.
     *
     * @param id id of the location
     * @return LocationResponse object
     */
    public LocationResponse getLocation(long id) {
        return get(
                API_URL + "/locations/" + id,
                LocationResponse.class);
    }

    /**
     * Get all available images.
     *
     * @return ImagesResponse object
     */
    public ImagesResponse getImages() {
        return getImages(new PaginationParameters(null, null));
    }

    /**
     * Get all available images.
     *
     * @param paginationParameters Pagination parametres
     * @return ImagesResponse object
     */
    public ImagesResponse getImages(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/images")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ImagesResponse.class);
    }

    /**
     * Get all images by type.
     *
     * @param type Type of image
     * @return ImagesResponse object
     */
    public ImagesResponse getImages(ImageType type) {
        return getImages(type, new PaginationParameters(null, null));
    }

    /**
     * Get all images by type.
     *
     * @param type                 Type of image
     * @param paginationParameters Pagination parametres
     * @return ImagesResponse object
     */
    public ImagesResponse getImages(ImageType type, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/images")
                        .queryParam("type", type.toString())
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                ImagesResponse.class);
    }

    /**
     * Get an image by name.
     *
     * @param name Name of the image
     * @return ImagesResponse object
     */
    public ImagesResponse getImageByName(String name) {
        return get(
                API_URL + "/images?name=" + name,
                ImagesResponse.class);
    }

    /**
     * Get image by ID.
     *
     * @param id ID of the image
     * @return ImageResponse object
     */
    public ImageResponse getImage(long id) {
        return get(
                API_URL + "/images/" + id,
                ImageResponse.class);
    }

    /**
     * Update the description or the type of a image.
     *
     * @param id                 ID of the image
     * @param updateImageRequest Request object
     * @return ImageResponse object
     */
    public ImageResponse updateImage(long id, UpdateImageRequest updateImageRequest) {
        return put(
                API_URL + "/images/" + id,
                updateImageRequest,
                ImageResponse.class);
    }

    /**
     * Delete an image,
     * <p>
     * This object does not have a respond!
     *
     * @param id ID of the image
     * @return String
     */
    public String deleteImage(long id) {
        return delete(
                API_URL + "/images/" + id,
                String.class);
    }

    /**
     * Get all volumes in a project.
     *
     * @return GetVolumesResponse
     */
    public GetVolumesResponse getVolumes() {
        return getVolumes(new PaginationParameters(null, null));
    }

    /**
     * Get all volumes in a project.
     *
     * @param paginationParameters Pagination parametres.
     * @return GetVolumesResponse
     */
    public GetVolumesResponse getVolumes(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/volumes")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                GetVolumesResponse.class);
    }

    /**
     * Get a specific volume by id.
     *
     * @param id ID of the volume
     * @return Volume object
     */
    public GetVolumeResponse getVolume(long id) {
        return get(
                API_URL + "/volumes/" + id,
                GetVolumeResponse.class);
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
        return post(
                API_URL + "/volumes",
                volumeRequest,
                VolumeResponse.class);
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
        return put(
                API_URL + "/volumes/" + id,
                updateVolumeRequest,
                GetVolumeResponse.class);
    }

    /**
     * Delete a volume
     *
     * @param id ID of the volume
     * @return no return object
     */
    public String deleteVolume(long id) {
        return delete(
                API_URL + "/volumes/" + id,
                String.class);
    }

    /**
     * Get all actions of a volume.
     *
     * @param id ID of the volume
     * @return Action array
     */
    public ActionsResponse getAllActionsOfVolume(long id) {
        return get(
                API_URL + "/volumes/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Get a specific action of a volume.
     *
     * @param id       ID of the volume
     * @param actionID ID of the action
     * @return Action object
     */
    public ActionResponse getActionOfVolume(long id, long actionID) {
        return get(
                API_URL + "/volumes/" + id + "/actions/" + actionID,
                ActionResponse.class);
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
        return post(
                API_URL + "/volumes/" + id + "/actions/attach",
                attachVolumeRequest,
                ActionResponse.class);
    }

    /**
     * Detach a volume from a server.
     *
     * @param id ID of the volume
     * @return Action object
     */
    public ActionResponse detachVolume(long id) {
        return post(
                API_URL + "/volumes/" + id + "/actions/detach",
                ActionResponse.class);
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
        return post(
                API_URL + "/volumes/" + id + "/actions/resize",
                resizeVolumeRequest,
                ActionResponse.class);
    }

    /**
     * Change the protection mode of the volume.
     * Only deletion protection is available!
     *
     * @param id                      ID of the volume
     * @param changeProtectionRequest Request object
     * @return Action object
     */
    public ActionResponse changeVolumeProtection(long id, ChangeProtectionRequest changeProtectionRequest) {
        if (changeProtectionRequest.isRebuild())
            throw new InvalidParametersException("Rebuild can't be used on volumes.");
        return post(
                API_URL + "/volumes/" + id + "/actions/change_protection",
                changeProtectionRequest,
                ActionResponse.class);
    }

    /**
     * Get all networks which are in project.
     *
     * @return Response from API
     * @deprecated This method is deprecated and will be removed with a future release. Please use {@link #getNetworks()} instead
     */
    @Deprecated
    public NetworksResponse getAllNetworks() {
        return get(
                API_URL + "/networks",
                NetworksResponse.class);
    }

    /**
     * Get all Private networks in a project.
     *
     * @return NetworksResponse
     */
    public NetworksResponse getNetworks() {
        return getNetworks(new PaginationParameters(null, null));
    }

    /**
     * Get all Private networks in a project.
     *
     * @param paginationParameters Pagination parametres
     * @return NetworksResponse
     */
    public NetworksResponse getNetworks(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/networks")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                NetworksResponse.class);
    }

    /**
     * Get all networks with specific name.
     *
     * @param name Name of the network
     * @return Response from API
     */
    public NetworksResponse getNetworksByName(String name) {
        return get(
                API_URL + "/networks?name=" + name,
                NetworksResponse.class);
    }

    /**
     * Get all networks with specific label.
     *
     * @param label Label which is attached to network
     * @return Response from API
     */
    public NetworksResponse getNetworksByLabel(String label) {
        return get(
                API_URL + "/networks?label_selector=" + label,
                NetworksResponse.class);
    }

    /**
     * Get a network with it's specific ID.
     *
     * @param id ID of the network
     * @return Response from API
     */
    public NetworkResponse getNetwork(long id) {
        return get(
                API_URL + "/networks/" + id,
                NetworkResponse.class);
    }

    /**
     * Update the labels or the name of a network.
     *
     * @param id                   ID of the network
     * @param updateNetworkRequest Request object
     * @return Response from API
     */
    public NetworkResponse updateNetwork(long id, UpdateNetworkRequest updateNetworkRequest) {
        return put(
                API_URL + "/networks/" + id,
                updateNetworkRequest,
                NetworkResponse.class);
    }


    /**
     * Create a new private network.
     *
     * @param networkRequest Request object
     * @return Response from API
     */
    public NetworkResponse createNetwork(NetworkRequest networkRequest) {
        if (networkRequest.getIpRange() == null) throw new InvalidParametersException("IP-Range missing!");
        if (networkRequest.getName() == null) throw new InvalidParametersException("Name missing!");
        return post(
                API_URL + "/networks",
                networkRequest,
                NetworkResponse.class);
    }

    /**
     * Delete a network.
     *
     * @param id ID of the network
     * @return There is no response.
     */
    public String deleteNetwork(long id) {
        return delete(
                API_URL + "/networks/" + id,
                String.class);
    }

    /**
     * Attaches a server to a private network.
     *
     * @param id                           ID of the server
     * @param attachServerToNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse attachServerToNetwork(long id, AttachServerToNetworkRequest attachServerToNetworkRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/attach_to_network",
                attachServerToNetworkRequest,
                ActionResponse.class);
    }


    /**
     * Detaches a server from a private network.
     *
     * @param id                             ID of the server
     * @param detachServerFromNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse detachServerFromNetwork(long id, DetachServerFromNetworkRequest detachServerFromNetworkRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/detach_from_network",
                detachServerFromNetworkRequest,
                ActionResponse.class);
    }

    /**
     * Change alias IPs of a network.
     *
     * @param id                             ID of the server
     * @param changeAliasIPsofNetworkRequest Request object
     * @return Response from API
     */
    public ActionResponse changeAliasIPsofNetwork(long id, ChangeAliasIPsofNetworkRequest changeAliasIPsofNetworkRequest) {
        return post(
                API_URL + "/servers/" + id + "/actions/change_alias_ips",
                changeAliasIPsofNetworkRequest,
                ActionResponse.class);
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
        return post(
                API_URL + "/networks/" + id + "/actions/change_protection",
                changeProtection,
                ActionResponse.class);
    }

    /**
     * Get all performed Actions for a network
     *
     * @param id ID of the network
     * @return Response from API
     */
    public ActionsResponse getActionsForNetwork(long id) {
        return get(
                API_URL + "/networks/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Get an action for a network
     *
     * @param serverID ID of the network
     * @param actionID ID of the Action
     * @return Response from API
     */
    public ActionResponse getActionForNetwork(long serverID, long actionID) {
        return get(
                API_URL + "/networks/" + serverID + "/actions/" + actionID,
                ActionResponse.class);
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
        return post(
                API_URL + "/networks/" + id + "/actions/add_subnet",
                addSubnetToNetworkRequest,
                ActionResponse.class);
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
        return post(
                API_URL + "/networks/" + id + "/actions/delete_subnet",
                deleteSubnetFromNetwork,
                ActionResponse.class);
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
        return post(
                API_URL + "/networks/" + id + "/actions/add_route",
                routeToNetwork,
                ActionResponse.class);
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
        return post(
                API_URL + "/networks/" + id + "/actions/delete_route",
                routeToNetwork,
                ActionResponse.class);
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
        return post(
                API_URL + "/networks/" + id + "/actions/change_ip_range",
                changeIPRangeOfNetwork,
                ActionResponse.class);
    }

    /**
     * Get all certificates from the project.
     *
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates() {
        return getCertificates(new PaginationParameters(null, null));
    }

    /**
     * Get all certificates from the project.
     *
     * @param paginationParameters Pagination parametres
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/certificates")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                CertificatesResponse.class);
    }

    /**
     * Get all certificates by label selector.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector Label selector used for filtering
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates(String labelSelector) {
        return getCertificates(labelSelector, new PaginationParameters(null, null));
    }

    /**
     * Get all certificates by label selector.
     * A label selector can be e.g. env=prod
     *
     * @param labelSelector        Label selector used for filtering
     * @param paginationParameters Pagination parametres
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificates(String labelSelector, PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/certificates")
                        .queryParam("label_selector", labelSelector)
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                CertificatesResponse.class);
    }

    /**
     * Get a specific certificate by its name.
     *
     * @param name Name of the certificate
     * @return CertificatesResponse
     */
    public CertificatesResponse getCertificate(String name) {
        return get(
                UrlBuilder.from(API_URL + "/certificates")
                        .queryParam("name", name)
                        .toUri(),
                CertificatesResponse.class);
    }

    /**
     * Get a specific certificate by id.
     *
     * @param id ID of the certificate
     * @return CertificateResponse
     */
    public CertificateResponse getCertificate(long id) {
        return get(
                API_URL + "/certificates/" + id,
                CertificateResponse.class);
    }

    /**
     * Create a new certificate.
     *
     * @param createCertificateRequest CertificateRequest object with name, public- and private-key
     * @return CertificateResponse
     */
    public CertificateResponse createCertificate(CreateCertificateRequest createCertificateRequest) {
        return post(
                API_URL + "/certificates",
                createCertificateRequest,
                CertificateResponse.class);
    }

    /**
     * Update a certificate.
     * <p>
     * Available options to update:
     * - Name
     * - Labels
     *
     * @param id                       ID of the certificate
     * @param updateCertificateRequest Certificate Update object
     * @return CertificateResponse
     */
    public CertificateResponse updateCertificate(long id, UpdateCertificateRequest updateCertificateRequest) {
        return put(
                API_URL + "/certificates/" + id,
                updateCertificateRequest,
                CertificateResponse.class);
    }

    /**
     * Retry an issuance or renewal for a managed certificate.
     * <p>
     * This method is only applicable to managed certificate where either the issuance
     * or renewal status is failed.
     *
     * @param id ID of the certificate
     * @return ActionResponse
     */
    public ActionResponse retryCertificate(long id) {
        return post(
                API_URL + "/certificates/" + id + "/actions/retry",
                ActionResponse.class);
    }

    /**
     * Delete a certificate.
     *
     * @param id ID of the certificate
     * @return nothing...
     */
    public String deleteCertificate(long id) {
        return delete(
                API_URL + "/certificates/" + id,
                String.class);
    }

    /**
     * Get all Load Balancers.
     *
     * @return LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancers() {
        return getLoadBalancers(new PaginationParameters(null, null));
    }

    /**
     * Get all Load Balancers.
     *
     * @param paginationParameters Pagination parametres
     * @return LoadBalancersResponse
     */
    public LoadBalancersResponse getLoadBalancers(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/load_balancers")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                LoadBalancersResponse.class);
    }

    /**
     * Get a specific Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse getLoadBalancer(long id) {
        return get(
                API_URL + "/load_balancers/" + id,
                LoadBalancerResponse.class);
    }

    /**
     * Create a new Load Balancer.
     *
     * @param loadBalancerRequest Load Balancer Request object
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse createLoadBalancer(LoadBalancerRequest loadBalancerRequest) {
        return post(
                API_URL + "/load_balancers",
                loadBalancerRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Update an existing Load Balancer.
     *
     * @param id                        ID of the Load Balancer
     * @param updateLoadBalancerRequest Load Balancer Update Request Object
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse updateLoadBalancer(long id, UpdateLoadBalancerRequest updateLoadBalancerRequest) {
        return put(
                API_URL + "/load_balancers/" + id,
                updateLoadBalancerRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Delete a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return nothing
     */
    public String deleteLoadBalancer(long id) {
        return delete(
                API_URL + "/load_balancers/" + id,
                String.class);
    }

    /**
     * Get all actions of a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return ActionsResponse
     */
    public ActionsResponse getAllActionsOfLoadBalancer(long id) {
        return get(
                API_URL + "/load_balancers/" + id + "/actions",
                ActionsResponse.class);
    }

    /**
     * Get an action of a Load Balancer.
     *
     * @param id       ID of the Load Balancer
     * @param actionId Action ID
     * @return ActionResponse
     */
    public ActionResponse getActionOfLoadBalancer(long id, long actionId) {
        return get(
                API_URL + "/load_balancers/" + id + "/actions/" + actionId,
                ActionResponse.class);
    }

    /**
     * Add a service to a Load Balancer.
     *
     * @param id               ID of the Load Balancer
     * @param lbServiceRequest Load Balancer Service Request
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse addServiceToLoadBalancer(long id, LBServiceRequest lbServiceRequest) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/add_service",
                lbServiceRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Update a service of a Load Balancer.
     *
     * @param id               ID of the Load Balancer
     * @param lbServiceRequest Load Balancer Service Request
     * @return LoadBalancerResponse
     */
    public LoadBalancerResponse updateServiceOfLoadBalancer(long id, LBServiceRequest lbServiceRequest) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/update_service",
                lbServiceRequest,
                LoadBalancerResponse.class);
    }

    /**
     * Delete a service of a Load Balancer.
     *
     * @param id         ID of the Load Balancer
     * @param listenPort The desired "listen port" of the service
     * @return ActionResponse
     */
    public ActionResponse deleteServiceOfLoadBalancer(long id, long listenPort) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/delete_service",
                new LoadBalancerDeleteServiceRequest(listenPort),
                ActionResponse.class);
    }

    /**
     * Add a target to a Load Balancer.
     *
     * @param id              ID of the Load Balancer
     * @param lbTargetRequest Load Balancer Target Request
     * @return ActionResponse
     */
    public ActionResponse addTargetToLoadBalancer(long id, LBTargetRequest lbTargetRequest) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/add_target",
                lbTargetRequest,
                ActionResponse.class);
    }

    /**
     * Removes a target from a load balancer.
     *
     * @param id              ID of the Load Balancer
     * @param lbTargetRequest Load Balancer Target Request
     * @return ActionResponse
     */
    public ActionResponse removeTargetFromLoadBalancer(long id, LBTargetRequest lbTargetRequest) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/remove_target",
                lbTargetRequest,
                ActionResponse.class);
    }

    /**
     * Changes the algorithm that determines to which target new requests are sent.
     *
     * @param id            ID of the Load Balancer
     * @param algorithmType Algorithm Type
     * @return ActionResponse
     */
    public ActionResponse changeAlgorithmOfLoadBalancer(long id, String algorithmType) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/change_algorithm",
                new LoadBalancerChangeAlgorithmRequest(algorithmType),
                ActionResponse.class);
    }


    /**
     * Changes the type of a Load Balancer.
     *
     * @param id               ID of the Load Balancer
     * @param loadBalancerType New type of the Load Balancer
     * @return ActionResponse
     */
    public ActionResponse changeTypeOfLoadBalancer(long id, String loadBalancerType) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/change_type",
                new LoadBalancerChangeTypeRequest(loadBalancerType),
                ActionResponse.class);
    }

    /**
     * Attach a network to a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkId ID of the Network
     * @param ip        IP for the Load Balancer in this private network
     * @return ActionResponse
     */
    public ActionResponse attachNetworkToLoadBalancer(long id, long networkId, String ip) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/attach_to_network",
                new LoadBalancerNetworkRequest(networkId, ip),
                ActionResponse.class);
    }

    /**
     * Attach a network to a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkId ID of the Network
     * @return ActionResponse
     */
    public ActionResponse attachNetworkToLoadBalancer(long id, long networkId) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/attach_to_network",
                new LoadBalancerNetworkRequest(networkId),
                ActionResponse.class);
    }

    /**
     * Detach a network from a Load Balancer.
     *
     * @param id        ID of the Load Balancer
     * @param networkId ID of the Network
     * @return ActionResponse
     */
    public ActionResponse detachNetworkFromLoadBalancer(long id, long networkId) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/detach_from_network",
                new LoadBalancerNetworkRequest(networkId),
                ActionResponse.class);
    }

    /**
     * Enable the public interface of a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return ActionResponse
     */
    public ActionResponse enablePublicInterfaceOfLoadBalancer(long id) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/enable_public_interface",
                ActionResponse.class);
    }

    /**
     * Disable the public interface of a Load Balancer.
     *
     * @param id ID of the Load Balancer
     * @return ActionResponse
     */
    public ActionResponse disablePublicInterfaceOfLoadBalancer(long id) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/disable_public_interface",

                ActionResponse.class);
    }

    /**
     * Change the protection configuration of a Load Balancer.
     *
     * @param id     ID of the Load Balancer
     * @param delete Delete protection
     * @return ActionResponse
     */
    public ActionResponse changeProtectionOfLoadBalancer(long id, boolean delete) {
        return post(
                API_URL + "/load_balancers/" + id + "/actions/change_protection",
                new LoadBalancerChangeProtectionRequest(delete),
                ActionResponse.class);
    }

    /**
     * Get a specific placement group.
     *
     * @param id placement group ID
     * @return PlacementGroupResponse
     */
    public PlacementGroupResponse getPlacementGroup(long id) {
        return get(
                API_URL + "/placement_groups/" + id,
                PlacementGroupResponse.class);
    }

    /**
     * Get all placement groups.
     *
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroups() {
        return getPlacementGroups(new PaginationParameters(null, null));
    }

    /**
     * Get all placement groups.
     *
     * @param paginationParameters Pagination parametres
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroups(PaginationParameters paginationParameters) {
        return get(
                UrlBuilder.from(API_URL + "/placement_groups")
                        .queryParamIfPresent("page", Optional.ofNullable(paginationParameters.page))
                        .queryParamIfPresent("per_page", Optional.ofNullable(paginationParameters.perPage))
                        .toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Get placement group by name.
     *
     * @param name name of the placement grouo
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroupByName(String name) {
        UrlBuilder builder = UrlBuilder.from(API_URL + "/placement_groups")
                .queryParam("name", name);

        return get(
                builder.toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Get placement groups by label selector.
     *
     * @param labelSelector label selector used by resource
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroupByLabelSelector(String labelSelector) {
        UrlBuilder builder = UrlBuilder.from(API_URL + "/placement_groups")
                .queryParam("label_selector", labelSelector);

        return get(
                builder.toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Get placement groups by type
     *
     * @param type Type of the placement group
     * @return PlacementGroupsResponse
     */
    public PlacementGroupsResponse getPlacementGroupByLabelSelector(PlacementGroupType type) {
        return get(
                UrlBuilder.from(API_URL + "/placement_groups").queryParam("type", type.toString()).toUri(),
                PlacementGroupsResponse.class);
    }

    /**
     * Create a placement group.
     *
     * @param placementGroupRequest PlacementGroupRequest object
     * @return PlacementGroupResponse
     */
    public PlacementGroupResponse createPlacementGroup(PlacementGroupRequest placementGroupRequest) {
        return post(
                API_URL + "/placement_groups",
                placementGroupRequest,
                PlacementGroupResponse.class);
    }

    /**
     * Delete a placement group.
     *
     * @param id placement group ID
     * @return ActionResponse
     */
    public String deletePlacementGroup(long id) {
        return delete(
                API_URL + "/placement_groups/" + id,
                String.class);
    }

    /**
     * Converts a Date to the ISO-8601 format
     *
     * @param date Date to be converted
     * @return Date in ISO-8601 format
     */
    public String convertToISO8601(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    private <T> T exchange(String url, HttpMethod method, Object body, Class<T> clazz) {
        try {
            RequestBody requestBody = null;

            if (body != null) {
                requestBody = RequestBody.create(objectMapper.writeValueAsBytes(body), MediaType.get("application/json"));
            }

            var response = client.newCall(new Request.Builder()
                    .addHeader("Authorization", "Bearer " + hcloudToken)
                    .addHeader("Accept", "application/json")
                    .url(url)
                    .method(method.toString(), requestBody).build()).execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException(response.body().string());
            }

            if (String.class.equals(clazz)) {
                return (T) response.body().string();
            } else {
                return objectMapper.readValue(response.body().bytes(), clazz);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T get(String url, Class<T> clazz) {
        return exchange(url, HttpMethod.GET, null, clazz);
    }

    private <T> T delete(String url, Class<T> clazz) {
        return exchange(url, HttpMethod.DELETE, null, clazz);
    }

    private <T> T put(String url, Object body, Class<T> clazz) {
        return exchange(url, HttpMethod.PUT, body, clazz);
    }

    private <T> T post(String url, Object body, Class<T> clazz) {
        return exchange(url, HttpMethod.POST, body, clazz);
    }

    private <T> T post(String url, Class<T> clazz) {
        return exchange(url, HttpMethod.POST, null, clazz);
    }

    private enum HttpMethod {
        GET, PUT, POST, DELETE
    }

}