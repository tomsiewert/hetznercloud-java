package me.tomsdevsn.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeTypeRequest {

    @JsonProperty("upgrade_disk")
    private boolean upgradeDisk;
    @JsonProperty("server_type")
    private String serverType;

    @Getter
    @AllArgsConstructor
    public enum ServerType {

        CX11("cx11"),
        CX11_CEPH("cx11-ceph"),
        CX21("cx21"),
        CX21_CEPH("cx21-ceph"),
        CX31("cx31"),
        CX31_CEPH("cx31-ceph"),
        CX41("cx41"),
        CX41_CEPH("cx41-ceph"),
        CX51("cx51"),
        CX51_CEPH("cx51-ceph"),
        CCX11("ccx11"),
        CCX21("ccx21"),
        CCX31("ccx31"),
        CCX41("ccx41"),
        CCX51("ccx51"),
        CPX11("cpx11"),
        CPX21("cpx21"),
        CPX31("cpx31"),
        CPX41("cpx41"),
        CPX51("cpx51");

        private String type;

        @Override
        public String toString() {
            return type;
        }
    }
}
