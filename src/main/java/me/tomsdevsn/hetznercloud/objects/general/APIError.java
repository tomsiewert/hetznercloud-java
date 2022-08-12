package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class APIError {

    private ErrorCode code;
    private String message;
    private JsonNode details;

}
