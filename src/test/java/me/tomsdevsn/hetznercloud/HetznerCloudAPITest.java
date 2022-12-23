package me.tomsdevsn.hetznercloud;

import lombok.extern.slf4j.Slf4j;
import me.tomsdevsn.hetznercloud.objects.enums.*;
import me.tomsdevsn.hetznercloud.objects.general.*;
import me.tomsdevsn.hetznercloud.objects.request.*;
import me.tomsdevsn.hetznercloud.objects.response.CreatePrimaryIPResponse;
import me.tomsdevsn.hetznercloud.objects.response.ISOResponse;
import me.tomsdevsn.hetznercloud.objects.response.ISOSResponse;
import me.tomsdevsn.hetznercloud.objects.response.PlacementGroupResponse;
import org.awaitility.Awaitility;
import org.jclouds.ssh.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class HetznerCloudAPITest {

    private final HetznerCloudAPI hetznerCloudAPI = new HetznerCloudAPI(System.getenv("HCLOUD_TOKEN"));

    private final String testUUID = UUID.randomUUID().toString();
    private final String testUUIDLabelKey = "java/testID";
    private final String testUUIDLabelSelector = String.format("%s=%s", testUUIDLabelKey, testUUID);

    @AfterAll
    public void cleanUpAfter() {
        log.info("cleanup e2e resources");

        log.info("removing certificates");
        hetznerCloudAPI.getCertificates(testUUIDLabelSelector).getCertificates().forEach((certificate -> {
            log.info("removing certificate '{}'", certificate.getName());
            hetznerCloudAPI.deleteCertificate(certificate.getId());
        }));

        log.info("removing ssh keys");
        hetznerCloudAPI.getSSHKeys(testUUIDLabelSelector).getSshKeys().forEach((sshKey -> {
            log.info("removing ssh key '{}'", sshKey.getName());
            hetznerCloudAPI.deleteSSHKey(sshKey.getId());
        }));

        log.info("removing volumes");
        hetznerCloudAPI.getVolumes(testUUIDLabelSelector).getVolumes().forEach((volume -> {

            hetznerCloudAPI.getVolumeActions(volume.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' to finish for volume '{}'", action.getCommand(), volume.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
            }));

            log.info("removing volume '{}'", volume.getName());
            hetznerCloudAPI.deleteVolume(volume.getId());
        }));

        log.info("removing loadbalancers");
        hetznerCloudAPI.getLoadBalancers(testUUIDLabelSelector).getLoadBalancers().forEach((loadBalancer -> {

            hetznerCloudAPI.getLoadBalancerActions(loadBalancer.getId()).getActions().forEach((action -> {
                log.info("waiting for action '{}' fo finish for loadbalancer '{}'", action.getCommand(), loadBalancer.getName());
                Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
            }));

            log.info("removing loadbalancer '{}'", loadBalancer.getName());
            hetznerCloudAPI.deleteLoadBalancer(loadBalancer.getId());
        }));

        log.info("removing networks");
        hetznerCloudAPI.getNetworks(testUUIDLabelSelector).getNetworks().forEach((network -> {
            log.info("removing network '{}'", network.getName());
            hetznerCloudAPI.deleteNetwork(network.getId());
        }));

        log.info("removing primary ips");
        hetznerCloudAPI.getPrimaryIPs(testUUIDLabelSelector).getPrimaryIPs().forEach(ip -> {
            log.info("removing primary ip '{}'", ip.getId());
            hetznerCloudAPI.deletePrimaryIP(ip.getId());
        });
    }

    @Test
    void testPrimaryIPCreation() {
        var name = UUID.randomUUID().toString();

        CreatePrimaryIPResponse primaryIP = hetznerCloudAPI.createPrimaryIP(
                CreatePrimaryIPRequest.builder()
                        .name(name)
                        .datacenter("fsn1-dc14")
                        .type(IPType.ipv6)
                        .assigneeType(IPAssigneeType.server)
                        .label("ilike", "cheesecake")
                        .label(testUUIDLabelKey, testUUID)
                .build());

        assertThat(primaryIP).isNotNull();
        assertThat(primaryIP.getPrimaryIP()).isNotNull();

        assertThat(primaryIP.getPrimaryIP().getName()).isEqualTo(name);
        assertThat(primaryIP.getPrimaryIP().getType()).isEqualTo(IPType.ipv6);
        assertThat(primaryIP.getPrimaryIP().getLabels().get("ilike")).isEqualTo("cheesecake");

        hetznerCloudAPI.deletePrimaryIP(primaryIP.getPrimaryIP().getId());
    }

    @Test
    void getISOs() {
        ISOSResponse isos = hetznerCloudAPI.getISOS();
        assertNotNull(isos);
        assertNotNull(isos.getIsos());
    }

    @Test
    void getISOById() {
        ISOResponse iso = hetznerCloudAPI.getISO(
                hetznerCloudAPI.getISOS().getIsos().stream().findFirst().get().getId()
        );

        assertNotNull(iso);
        assertNotNull(iso.getIso());
    }

    @Test
    void getFirewalls() {
        final var firewalls = hetznerCloudAPI.getFirewalls();
    }

    @Test
    void createFirewall() {
        final var firewallRequest = CreateFirewallRequest.builder()
                .applicationTarget(FWApplicationTarget.builder()
                        .labelSelector(new FWLabelSelector("env==dev"))
                        .type(TargetType.label_selector)
                        .build())
                //.applicationTarget(FWApplicationTarget.builder()
                //        .server(new FWServerRef(12345678L))
                //        .type(TargetType.server)
                //        .build())
                .label("test", "firewall")
                .label(testUUIDLabelKey, testUUID)
                .name(UUID.randomUUID().toString())
                .firewallRule(FirewallRule.builder()
                        .description("test-fw-rule-http")
                        .desinationIP("0.0.0.0/0")
                        .port("80")
                        .direction(FirewallRule.Direction.out)
                        .protocol(FirewallRule.Protocol.tcp)
                        .build())
                .firewallRule(FirewallRule.builder()
                        .description("test-fw-rule-https")
                        .desinationIP("0.0.0.0/0")
                        .port("443")
                        .direction(FirewallRule.Direction.out)
                        .protocol(FirewallRule.Protocol.tcp)
                        .build())
                .firewallRule(FirewallRule.builder()
                        .description("test-fw-rule-icmp")
                        .desinationIP("0.0.0.0/0")
                        .direction(FirewallRule.Direction.out)
                        .protocol(FirewallRule.Protocol.icmp)
                        .build())
                .build();
        final var response = hetznerCloudAPI.createFirewall(firewallRequest);
    }

    @Test
    void deleteFirewall() {
        final List<Firewall> firewalls = hetznerCloudAPI.getFirewalls(testUUIDLabelSelector).getFirewalls();
        assertThat(firewalls).hasSizeGreaterThanOrEqualTo(1);

        hetznerCloudAPI.deleteFirewall(firewalls.get(0).getId());
        assertThat(hetznerCloudAPI.getFirewalls().getFirewalls())
                .hasSize(firewalls.size() - 1);
    }

    @Test
    void testFloatingIPCreation() {
        FloatingIP floatingIp = hetznerCloudAPI.createFloatingIP(CreateFloatingIPRequest.builder()
                .name(UUID.randomUUID().toString())
                .label(testUUIDLabelKey, testUUID)
                .type(IPType.ipv6)
                .homeLocation("fsn1")
                .build()).getFloatingIP();
    }

    @Test
    void testManageSSHKeys() {
        Map<String, String> keyPair = SshKeys.generate();

        String keyId = UUID.randomUUID().toString();

        // create key
        var createdKey = hetznerCloudAPI.createSSHKey(
                CreateSSHKeyRequest.builder()
                        .name(keyId)
                        .publicKey(keyPair.get("public"))
                        .label("label1", "value1")
                        .label(testUUIDLabelKey, testUUID)
                        .build());
        assertThat(createdKey).isNotNull();
        assertThat(createdKey.getSshKey()).isNotNull();

        var sshKeys = hetznerCloudAPI.getSSHKey(keyId);

        assertThat(sshKeys.getSshKeys().get(0).getLabels()).hasSize(2);
        assertThat(sshKeys.getSshKeys().get(0).getLabels().get("label1")).isEqualTo("value1");

        // update key
        hetznerCloudAPI.updateSSHKey(
                createdKey.getSshKey().getId(),
                UpdateSSHKeyRequest.builder()
                        .label(testUUIDLabelKey, testUUID)
                        .label("label2", "value2")
                        .build());

        var sshKey1 = hetznerCloudAPI.getSSHKey(createdKey.getSshKey().getId());
        assertThat(sshKey1.getSshKey().getLabels().get("label2")).isEqualTo("value2");
    }

    @Test
    void testManageVolumes() {
        // create volume
        String keyId = UUID.randomUUID().toString();
        var createdVolume = hetznerCloudAPI.createVolume(
                CreateVolumeRequest.builder()
                        .name(keyId)
                        .location("fsn1")
                        .label(testUUIDLabelKey, testUUID)
                        .size(10L)
                        .build());

        // wait for volume to settle
        createdVolume.getNextActions().forEach((action) -> {
            Awaitility.await().until(() -> {
                var actionResponse = hetznerCloudAPI.getAction(action.getId());
                return actionResponse.getAction().getFinished() != null;
            });
        });
        assertThat(hetznerCloudAPI.getVolumes(testUUIDLabelSelector).getVolumes()).hasSize(1);

        // get volume
        var volume = hetznerCloudAPI.getVolume(createdVolume.getVolume().getId());
        assertThat(volume.getVolume().getName()).isEqualTo(keyId);

    }

    @Test
    void testManageCertificates() throws IOException {
        var key = new String(HetznerCloudAPITest.class.getResourceAsStream("/certificates/key.pem").readAllBytes());
        var cert = new String(HetznerCloudAPITest.class.getResourceAsStream("/certificates/cert.pem").readAllBytes());

        String keyId = UUID.randomUUID().toString();

        var createdCertificate = hetznerCloudAPI.createCertificate(
                CreateCertificateRequest.builder()
                        .name(keyId)
                        .privateKey(key)
                        .certificate(cert)
                        .label(testUUIDLabelKey, testUUID)
                        .build());

        assertThat(createdCertificate).isNotNull();
        assertThat(createdCertificate.getCertificate().getId()).isGreaterThan(0);
        assertThat(hetznerCloudAPI.getCertificates(testUUIDLabelSelector).getCertificates()).hasSize(1);

        var certificate1 = hetznerCloudAPI.getCertificate(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getId()).isEqualTo(createdCertificate.getCertificate().getId());
        assertThat(certificate1.getCertificate().getName()).isEqualTo(keyId);
    }

    @Test
    void testBasicLoadBalancer() {
        String keyId = UUID.randomUUID().toString();

        var loadBalancerType = hetznerCloudAPI.getLoadBalancerTypes().getLoadBalancerTypes().get(0);
        assertNotNull(loadBalancerType);
        var loadBalancerRequest = CreateLoadBalancerRequest.builder()
                .loadBalancerType(loadBalancerType.getId().toString())
                .publicInterface(true)
                .name(keyId)
                .location("fsn1")
                .label(testUUIDLabelKey, testUUID)
                .build();

        // create loadbalancer
        var createdLoadbalancer = hetznerCloudAPI.createLoadBalancer(loadBalancerRequest);
        assertNotNull(createdLoadbalancer);
        assertNotNull(createdLoadbalancer.getLoadBalancer());
        assertThat(hetznerCloudAPI.getLoadBalancers(testUUIDLabelSelector).getLoadBalancers()).hasSize(1);

        // wait for all actions to finish
        hetznerCloudAPI.getLoadBalancerActions(createdLoadbalancer.getLoadBalancer().getId()).getActions().forEach((action) -> {
            Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
        });

        // get loadbalancer
        var loadblancer = hetznerCloudAPI.getLoadBalancer(createdLoadbalancer.getLoadBalancer().getId());
        assertNotNull(loadblancer.getLoadBalancer());
        assertThat(loadblancer.getLoadBalancer().getName()).isEqualTo(keyId);
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
    void testPlacementGroup() {
        String keyId = UUID.randomUUID().toString();
        CreatePlacementGroupRequest placementGroupRequest = CreatePlacementGroupRequest.builder()
                .name(keyId)
                .type(PlacementGroupType.spread)
                .label(testUUIDLabelKey, testUUID)
                .build();

        PlacementGroupResponse placementGroup = hetznerCloudAPI.createPlacementGroup(placementGroupRequest);
        assertNotNull(placementGroup);
        assertNotNull(placementGroup.getPlacementGroup());
    }
}
