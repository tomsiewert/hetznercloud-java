package me.tomsdevsn.hetznercloud;

import lombok.extern.slf4j.Slf4j;
import me.tomsdevsn.hetznercloud.objects.general.*;
import me.tomsdevsn.hetznercloud.objects.request.*;
import me.tomsdevsn.hetznercloud.objects.response.*;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class HetznerCloudAPITest {

    private final List<CleanupObject> cleanupObjects = new ArrayList<>();

    private final String testIdentifier = new Random().ints(48, 122)
            .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
            .mapToObj(i -> (char) i)
            .limit(8)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();

    private final HetznerCloudAPI hetznerCloudAPI = new HetznerCloudAPI(System.getenv("HCLOUD_TOKEN"));

    @AfterAll
    public void cleanUpAfter() {
        cleanup();
        cleanupObjects.forEach(cleanupObject -> {
            Object response = cleanup(cleanupObject);
            log.info("Cleanup " + cleanupObject + " response: " + response);
        });
    }

    @BeforeAll
    public void cleanUpBefore() {
        cleanup();
        cleanupObjects.forEach(cleanupObject -> {
            Object response = cleanup(cleanupObject);
            log.info("Cleanup " + cleanupObject + " response: " + response);
        });
    }

    private void cleanup() {
        log.info("cleaning testbed");

        log.info("removing certificates");
        hetznerCloudAPI.getCertificates().getCertificates().forEach((certificate -> {
            log.info("removing certificate '{}'", certificate.getName());
            hetznerCloudAPI.deleteCertificate(certificate.getId());
        }));

        log.info("removing ssh keys");
        hetznerCloudAPI.getSSHKeys().getSshKeys().forEach((sshKey -> {
            log.info("removing ssh key '{}'", sshKey.getName());
            hetznerCloudAPI.deleteSSHKey(sshKey.getId());
        }));

        log.info("removing volumes");
        hetznerCloudAPI.getVolumes().getVolumes().forEach((volume -> {

            hetznerCloudAPI.getAllActionsOfVolume(volume.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' to finish for volume '{}'", action.getCommand(), volume.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getActionOfVolume(volume.getId(), action.getId()).getAction().getFinished() != null);
            }));

            log.info("removing volume '{}'", volume.getName());
            hetznerCloudAPI.deleteVolume(volume.getId());
        }));

        log.info("removing loadbalancers");
        hetznerCloudAPI.getLoadBalancers().getLoadBalancers().forEach((loadBalancer -> {

            hetznerCloudAPI.getAllActionsOfLoadBalancer(loadBalancer.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' fo finish for loadbalancer '{}'", action.getCommand(), loadBalancer.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getActionOfLoadBalancer(loadBalancer.getId(), action.getId()).getAction().getFinished() != null);
            }));

            log.info("removing loadbalancer '{}'", loadBalancer.getName());
            hetznerCloudAPI.deleteLoadBalancer(loadBalancer.getId());
        }));

        log.info("removing networks");
        hetznerCloudAPI.getNetworks().getNetworks().forEach((network -> {
            log.info("removing network '{}'", network.getName());
            hetznerCloudAPI.deleteNetwork(network.getId());
        }));

        log.info("removing primary ips");
        hetznerCloudAPI.getPrimaryIPs().getPrimaryIPs().forEach(ip -> {
            log.info("removing primary ip '{}'", ip.getId());
            hetznerCloudAPI.deletePrimaryIP(ip.getId());
        });
    }

    private Object cleanup(CleanupObject cleanupObject) {
        switch (cleanupObject.getCleanupType()) {
            case SERVER:
                return hetznerCloudAPI.deleteServer(cleanupObject.getId());
            case IMAGE:
                return hetznerCloudAPI.deleteImage(cleanupObject.getId());
            case FLOATING_IP:
                return hetznerCloudAPI.deleteFloatingIP(cleanupObject.getId());
            case PLACEMENT_GROUP:
                return hetznerCloudAPI.deletePlacementGroup(cleanupObject.getId());
            case PRIMARY_IP:
                return hetznerCloudAPI.deletePrimaryIP(cleanupObject.getId());
            default:
                return null;
        }
    }

    @Test
    void testPrimaryIPCreation() {
        assertThat(hetznerCloudAPI.getPrimaryIPs().getPrimaryIPs()).hasSize(0);

        var name = UUID.randomUUID().toString();

        CreatePrimaryIPResponse primaryIP = hetznerCloudAPI.createPrimaryIP(
                CreatePrimaryIPRequest.builder()
                        .name(name)
                        .datacenter("fsn1-dc14")
                        .type(IPType.ipv6)
                        .assigneeType(IPAssigneeType.server)
                        .label("ilike", "cheesecake")
                .build());

        assertThat(primaryIP).isNotNull();
        assertThat(primaryIP.getPrimaryIP()).isNotNull();

        assertThat(primaryIP.getPrimaryIP().getName()).isEqualTo(name);
        assertThat(primaryIP.getPrimaryIP().getType()).isEqualTo(IPType.ipv6);
        assertThat(primaryIP.getPrimaryIP().getLabels().get("ilike")).isEqualTo("cheesecake");

        hetznerCloudAPI.deletePrimaryIP(primaryIP.getPrimaryIP().getId());

        assertThat(hetznerCloudAPI.getPrimaryIPs().getPrimaryIPs()).hasSize(0);
    }

    @Test
    void testManageServers() {


    }

    @Test
    void getServerByName() {
    }

    @Test
    void getServerById() {
    }

    @Test
    void createServer() {
    }

    @Test
    void deleteServer() {
    }

    @Test
    void changeServerName() {
    }

    @Test
    void requestConsole() {
    }

    @Test
    void changeServerProtection() {
    }

    @Test
    void getAllActionsOfServer() {
    }

    @Test
    void getActionOfServer() {
    }

    @Test
    void getActionsOfFloatingIP() {
    }

    @Test
    void getActionOfFloatingIP() {
    }

    @Test
    void powerOnServer() {
    }

    @Test
    void powerOffServer() {
    }

    @Test
    void softRebootServer() {
    }

    @Test
    void resetServer() {
    }

    @Test
    void shutdownServer() {
    }

    @Test
    void resetRootPassword() {
    }

    @Test
    void enableRescue() {
    }

    @Test
    void testEnableRescue() {
    }

    @Test
    void enableRescueAndReset() {
    }

    @Test
    void testEnableRescueAndReset() {
    }

    @Test
    void disableRescue() {
    }

    @Test
    void rebuildServer() {
    }

    @Test
    void changeServerType() {
    }

    @Test
    void getMetrics() {
    }

    @Test
    void createImage() {
    }

    @Test
    void changeImageProtection() {
    }

    @Test
    void enableBackup() {
    }

    @Test
    void testEnableBackup() {
    }

    @Test
    void disableBackup() {
    }

    @Test
    void getISOs() {
        ISOSResponse isos = hetznerCloudAPI.getISOS();
        assertNotNull(isos);
        assertNotNull(isos.getIsos());
    }

    @Test
    void getISOById() {
        ISOResponse iso = hetznerCloudAPI.getISOById(
                hetznerCloudAPI.getISOS().getIsos().stream().findFirst().get().getId()
        );

        assertNotNull(iso);
        assertNotNull(iso.getIso());
    }

    @Test
    void attachISO() {
    }

    @Test
    void detachISO() {
    }

    @Test
    void changeDNSPTR() {
    }

    @Test
    void getDatacenter() {
    }

    @Test
    void getDatacenters() {
    }

    @Test
    void getPricing() {
    }

    @Test
    void getFloatingIPs() {
    }

    @Test
    void getFloatingIP() {
    }

    @Test
    void createFloatingIP() {
    }

    @Test
    void changeFloatingIPProtection() {
    }

    @Test
    void changeDescriptionOfFloatingIP() {
    }

    @Test
    void assignFloatingIP() {
    }

    @Test
    void unassignFloatingIP() {
    }

    @Test
    void changeFloatingReverseDNS() {
    }

    @Test
    void deleteFloatingIP() {
    }

    @Test
    void updateFloatingIP() {
    }

    @Test
    void getSSHKeys() {
    }

    @Test
    void getSSHKey() {
    }

    @Test
    void getSSHKeyByName() {
    }

    @Test
    void getSSHKeyByFingerprint() {
    }

    @Test
    void testManageSSHKeys() {

        var publicKey = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPDS4NLE+d5jLs32yDDE4/JrCv8t0zk7tK3Z2nSQxPaj pelle@zoidberg";

        assertThat(hetznerCloudAPI.getSSHKeys().getSshKeys()).hasSize(0);

        // create key
        var createdKey = hetznerCloudAPI.createSSHKey(SSHKeyRequest.builder().name("sshkey1").publicKey(publicKey).label("label1", "value1").build());
        assertThat(createdKey).isNotNull();
        assertThat(createdKey.getSshKey()).isNotNull();

        // get key(s)
        var sshKeys = hetznerCloudAPI.getSSHKeyByName("sshkey1");

        assertThat(sshKeys.getSshKeys()).hasSize(1);
        assertThat(sshKeys.getSshKeys().get(0).getLabels()).hasSize(1);
        assertThat(sshKeys.getSshKeys().get(0).getLabels().get("label1")).isEqualTo("value1");

        // update key
        hetznerCloudAPI.updateSSHKey(createdKey.getSshKey().getId(), UpdateSSHKeyRequest.builder().name("new-sshkey1").build());

        var sshKey1 = hetznerCloudAPI.getSSHKey(createdKey.getSshKey().getId());
        assertThat(sshKey1.getSshKey().getName()).isEqualTo("new-sshkey1");

        // delete key
        hetznerCloudAPI.deleteSSHKey(createdKey.getSshKey().getId());
        assertThat(hetznerCloudAPI.getSSHKeys().getSshKeys()).hasSize(0);
    }

    @Test
    void getServerTypes() {
    }

    @Test
    void getAllLoadBalancerTypes() {
    }

    @Test
    void getAllLoadBalancerTypesByName() {
    }

    @Test
    void getServerTypeByName() {
    }

    @Test
    void getServerType() {
    }

    @Test
    void getLocations() {
    }

    @Test
    void getLocationByName() {
    }

    @Test
    void getLocation() {
    }

    @Test
    void getImages() {
    }

    @Test
    void testGetImages() {
    }

    @Test
    void getImageByName() {
    }

    @Test
    void getImage() {
    }

    @Test
    void updateImage() {
    }

    @Test
    void deleteImage() {
    }

    @Test
    void testManageVolumes() {

        assertThat(hetznerCloudAPI.getVolumes().getVolumes()).hasSize(0);

        // create volume
        var createdVolume = hetznerCloudAPI.createVolume(VolumeRequest.builder().name("volume1").location("fsn1").size(16L).build());

        // wait for volume to settle
        createdVolume.getNextActions().forEach((action) -> {
            Awaitility.await().until(() -> {
                var actionResponse = hetznerCloudAPI.getActionOfVolume(createdVolume.getVolume().getId(), action.getId());
                return actionResponse.getAction().getFinished() != null;
            });
        });
        assertThat(hetznerCloudAPI.getVolumes().getVolumes()).hasSize(1);

        // get volume
        var volume = hetznerCloudAPI.getVolume(createdVolume.getVolume().getId());
        assertThat(volume.getVolume().getName()).isEqualTo("volume1");

        // update volume
        hetznerCloudAPI.updateVolume(volume.getVolume().getId(), UpdateVolumeRequest.builder().name("new-volume1").build());
        volume = hetznerCloudAPI.getVolume(createdVolume.getVolume().getId());
        assertThat(volume.getVolume().getName()).isEqualTo("new-volume1");

        // wait for volume to settle
        createdVolume.getNextActions().forEach((action) -> {
            Awaitility.await().until(() -> {
                var actionResponse = hetznerCloudAPI.getActionOfVolume(createdVolume.getVolume().getId(), action.getId());
                return actionResponse.getAction().getFinished() != null;
            });
        });
    }

    @Test
    void deleteVolume() {
    }

    @Test
    void getAllActionsOfVolume() {
    }

    @Test
    void getActionOfVolume() {
    }

    @Test
    void attachVolumeToServer() {
    }

    @Test
    void detachVolume() {
    }

    @Test
    void resizeVolume() {
    }

    @Test
    void changeVolumeProtection() {
    }

    @Test
    void getAllNetworks() {
    }

    @Test
    void getNetworksByName() {
    }

    @Test
    void getNetworksByLabel() {
    }

    @Test
    void getNetwork() {
    }

    @Test
    void updateNetwork() {
    }

    @Test
    void createNetwork() {
    }

    @Test
    void deleteNetwork() {
    }

    @Test
    void attachServerToNetwork() {
    }

    @Test
    void detachServerFromNetwork() {
    }

    @Test
    void changeAliasIPsOfNetwork() {
    }

    @Test
    void changeNetworkProtection() {
    }

    @Test
    void getActionsForNetwork() {
    }

    @Test
    void getActionForNetwork() {
    }

    @Test
    void addSubnetToNetwork() {
    }

    @Test
    void deleteSubnetFromNetwork() {
    }

    @Test
    void addRouteToNetwork() {
    }

    @Test
    void deleteRouteFromNetwork() {
    }

    @Test
    void changeIPRangeOfNetwork() {
    }

    @Test
    void testManageCertificates() throws IOException {
        var key = new String(HetznerCloudAPITest.class.getResourceAsStream("/certificates/key.pem").readAllBytes());
        var cert = new String(HetznerCloudAPITest.class.getResourceAsStream("/certificates/cert.pem").readAllBytes());

        assertThat(hetznerCloudAPI.getCertificates().getCertificates()).hasSize(0);

        var createdCertificate = hetznerCloudAPI.createCertificate(CreateCertificateRequest.builder().name("certificate1").privateKey(key).certificate(cert).build());

        assertThat(createdCertificate).isNotNull();
        assertThat(createdCertificate.getCertificate().getId()).isGreaterThan(0);
        assertThat(hetznerCloudAPI.getCertificates().getCertificates()).hasSize(1);

        var certificate1 = hetznerCloudAPI.getCertificate(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getId()).isEqualTo(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getName()).isEqualTo("certificate1");

        hetznerCloudAPI.updateCertificate(certificate1.getCertificate().getId(), UpdateCertificateRequest.builder().name("new-certificate1").build());

        certificate1 = hetznerCloudAPI.getCertificate(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getName()).isEqualTo("new-certificate1");

        hetznerCloudAPI.deleteCertificate(certificate1.getCertificate().getId());

        assertThat(hetznerCloudAPI.getCertificates().getCertificates()).hasSize(0);

    }

    @Test
    void testManageLoadbalancers() {

        var networkName = testIdentifier + "-createLoadBalancer";
        var networkRequest = NetworkRequest.builder()
                .ipRange("10.0.0.0/16")
                .subnets(Collections.singletonList(getDefaultSubnet()))
                .name(networkName);
        var networkResponse = hetznerCloudAPI.createNetwork(networkRequest.build());
        assertNotNull(networkResponse);
        assertNotNull(networkResponse.getNetwork());

        var loadBalancerType = hetznerCloudAPI.getAllLoadBalancerTypes().getLoadBalancerTypes().get(0);
        assertNotNull(loadBalancerType);
        var loadBalancerName = testIdentifier + "-createLoadBalancer";
        var loadBalancerRequest = LoadBalancerRequest.builder()
                .networkZone("eu-central")
                .network(networkResponse.getNetwork().getId())
                .loadBalancerType(loadBalancerType.getId().toString())
                .publicInterface(true)
                .services(Collections.singletonList(getHttpService()))
                .targets(Collections.singletonList(getLabelSelector(loadBalancerName)))
                .name(loadBalancerName)
                .build();

        assertThat(hetznerCloudAPI.getLoadBalancers().getLoadBalancers()).hasSize(0);

        // create loadbalancer
        var createdLoadbalancer = hetznerCloudAPI.createLoadBalancer(loadBalancerRequest);
        assertNotNull(createdLoadbalancer);
        assertNotNull(createdLoadbalancer.getLoadBalancer());
        assertThat(hetznerCloudAPI.getLoadBalancers().getLoadBalancers()).hasSize(1);

        // wait for all actions to finish
        hetznerCloudAPI.getAllActionsOfLoadBalancer(createdLoadbalancer.getLoadBalancer().getId()).getActions().forEach((action) -> {
            Awaitility.await().until(() -> hetznerCloudAPI.getActionOfLoadBalancer(createdLoadbalancer.getLoadBalancer().getId(), action.getId()).getAction().getFinished() != null);
        });

        // get loadbalancer
        var loadblancer = hetznerCloudAPI.getLoadBalancer(createdLoadbalancer.getLoadBalancer().getId());
        assertNotNull(loadblancer.getLoadBalancer());
        assertThat(loadblancer.getLoadBalancer().getName()).isEqualTo(loadBalancerName);

        // update loadbalancer
        hetznerCloudAPI.updateLoadBalancer(loadblancer.getLoadBalancer().getId(), UpdateLoadBalancerRequest.builder().name("new-loadbalancer1").build());
        loadblancer = hetznerCloudAPI.getLoadBalancer(createdLoadbalancer.getLoadBalancer().getId());
        assertThat(loadblancer.getLoadBalancer().getName()).isEqualTo("new-loadbalancer1");

        // delete loadbalancer
        hetznerCloudAPI.deleteLoadBalancer(loadblancer.getLoadBalancer().getId());
        assertThat(hetznerCloudAPI.getLoadBalancers().getLoadBalancers()).hasSize(0);
    }

    private Subnet getDefaultSubnet() {
        var subnet = new Subnet();
        subnet.setType(SubnetType.cloud);
        subnet.setIpRange("10.0.0.0/24");
        subnet.setGateway("10.0.0.1");
        subnet.setNetworkZone("eu-central");
        return subnet;
    }

    private LBService getHttpService() {
        LBService lbService = new LBService();
        lbService.setProtocol("http");
        lbService.setDestinationPort(8080L);
        lbService.setListenPort(80L);
        lbService.setProxyprotocol(false);
        LBHealthCheck lbHealthCheck = new LBHealthCheck();
        LBHealthCheck.Http http = new LBHealthCheck.Http();
        http.setPath("/actuator/health");
        http.setDomain("");
        http.setResponse("");
        http.setTls(false);
        http.setStatusCodes(Arrays.asList("2??", "3??"));
        lbHealthCheck.setHttp(http);
        lbHealthCheck.setPort(8080L);
        lbHealthCheck.setProtocol("http");
        lbHealthCheck.setInterval(15L);
        lbHealthCheck.setTimeout(10L);
        lbHealthCheck.setRetries(3L);
        lbService.setHealthCheck(lbHealthCheck);
        return lbService;

    }

    private LBTarget getLabelSelector(String uniqueHostName) {
        LBTarget lbTarget = new LBTarget();
        lbTarget.setType(TargetType.label_selector);
        lbTarget.setLabelSelector(new LBTargetLabelSelector(uniqueHostName));
        lbTarget.setUsePrivateIp(true);
        return lbTarget;
    }


    @Test
    void addServiceToLoadBalancer() {
    }

    @Test
    void updateServiceOfLoadBalancer() {
    }

    @Test
    void deleteServiceOfLoadBalancer() {
    }

    @Test
    void addTargetToLoadBalancer() {
    }

    @Test
    void removeTargetFromLoadBalancer() {
    }

    @Test
    void changeAlgorithmOfLoadBalancer() {
    }

    @Test
    void changeTypeOfLoadBalancer() {
    }

    @Test
    void attachNetworkToLoadBalancer() {
    }

    @Test
    void testAttachNetworkToLoadBalancer() {
    }

    @Test
    void detachNetworkFromLoadBalancer() {
    }

    @Test
    void enablePublicInterfaceOfLoadBalancer() {
    }

    @Test
    void disablePublicInterfaceOfLoadBalancer() {
    }

    @Test
    void changeProtectionOfLoadBalancer() {
    }

    @Test
    void createPlacementGroup() {
        PlacementGroupResponse placementGroup = null;
        try {
            String name = testIdentifier + "-createPlacementGroup";
            PlacementGroupRequest placementGroupRequest = PlacementGroupRequest.builder()
                    .name(name)
                    .type(PlacementGroupType.spread)
                    .build();

            placementGroup = hetznerCloudAPI.createPlacementGroup(placementGroupRequest);
            assertNotNull(placementGroup);
            assertNotNull(placementGroup.getPlacementGroup());
        } catch (Exception e) {
            Assertions.fail(e);
        } finally {
            if (placementGroup != null)
                cleanupObjects.add(new CleanupObject(
                        placementGroup.getPlacementGroup().getId(),
                        CleanupType.PLACEMENT_GROUP));
        }
    }

}
