package me.tomsdevsn.hetznercloud;

import lombok.extern.slf4j.Slf4j;
import me.tomsdevsn.hetznercloud.objects.general.Action;
import me.tomsdevsn.hetznercloud.objects.general.Firewall;
import me.tomsdevsn.hetznercloud.objects.request.CreateFirewallRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActionsEndpointTest {

    private final HetznerCloudAPI hetznerCloudAPI = new HetznerCloudAPI(System.getenv("HCLOUD_TOKEN"));
    private final String ID_NOT_FOUND_MSG = "{\n" +
            "  \"error\": {\n" +
            "    \"message\": \"action with ID '%s' not found\",\n" +
            "    \"code\": \"not_found\",\n" +
            "    \"details\": null\n" +
            "  }\n" +
            "}\n";
    private final String INVALID_INPUT_MSG = "{\n" +
            "  \"error\": {\n" +
            "    \"message\": \"invalid action ID: '-1'\",\n" +
            "    \"code\": \"invalid_input\",\n" +
            "    \"details\": null\n" +
            "  }\n" +
            "}\n";

    private List<Action> forgedActions;
    private Firewall dummyFirewall;

    @BeforeAll
    void setUp() {
        final var dummyAction = hetznerCloudAPI.createFirewall(
                CreateFirewallRequest.builder()
                        .name(UUID.randomUUID().toString())
                        .build());

        dummyFirewall = dummyAction.getFirewall();
        forgedActions = dummyAction.getActions();
    }

    @AfterAll
    void tearDown() {
        hetznerCloudAPI.deleteFirewall(dummyFirewall.getId());
    }

    @Test
    void getAllActions() {
        final var actions = hetznerCloudAPI.getActions().getActions();
        assertThat(actions)
                .hasSizeGreaterThanOrEqualTo(forgedActions.size());
    }

    @Test
    void getAction_existingId() {
        assertThat(hetznerCloudAPI.getAction(forgedActions.get(0).getId()).getAction())
                .isEqualTo(forgedActions.get(0));
    }

    @Test
    void getAction_wrongId() {
        final int wrongId = 0;
        assertThatThrownBy(() -> hetznerCloudAPI.getAction(wrongId).getAction())
                .isInstanceOf(RuntimeException.class)
                .withFailMessage(ID_NOT_FOUND_MSG, wrongId);
    }

    @Test
    void getAction_invalidId() {
        final int invalidId = -1;
        assertThatThrownBy(() -> hetznerCloudAPI.getAction(invalidId).getAction())
                .isInstanceOf(RuntimeException.class)
                .withFailMessage(INVALID_INPUT_MSG, invalidId);
    }

}
