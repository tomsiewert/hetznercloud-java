package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FWApplicationTarget {

    @JsonProperty("label_selector")
    private FWLabelSelector labelSelector;
    private FWServerRef server;
    private TargetType type;

    public void isValidOrThrow() throws RuntimeException {
        final boolean isServerType = (getType() == TargetType.server);
        final boolean isServerNull = (getServer() == null);

        final boolean isLabelSelectorType = (getType() == TargetType.label_selector);
        final boolean isLabelSelectorNull = (getLabelSelector() == null);

        if (isServerType && (isServerNull || !isLabelSelectorNull))
            throw new RuntimeException(
                    "TargetType is \"server\", either \"server\" is null or \"labelSelector\" is not");

        if (isLabelSelectorType && (isLabelSelectorNull || !isServerNull))
            throw new RuntimeException(
                    "TargetType is \"label_selector\", either \"labelSelector\" is null or \"server\" is not");
    }

}
