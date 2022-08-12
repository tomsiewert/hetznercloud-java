package me.tomsdevsn.hetznercloud;

import lombok.extern.slf4j.Slf4j;
import me.tomsdevsn.hetznercloud.objects.general.Firewall;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FirewallsEndpointTest {

    private final HetznerCloudAPI hetznerCloudAPI = new HetznerCloudAPI(System.getenv("HCLOUD_TOKEN"));

    private List<Firewall> createdFirewalls;

    @BeforeAll
    void setUp() {
        createdFirewalls = new ArrayList<>();
    }

    @AfterAll
    void tearDown() {
        for (var firewall: createdFirewalls)
            hetznerCloudAPI.deleteFirewall(firewall.getId());
    }

}
