package me.tomsdevsn.hetznercloud.exception;

import lombok.Getter;
import me.tomsdevsn.hetznercloud.objects.response.APIErrorResponse;

public class APIRequestException extends RuntimeException {

    @Getter
    private final APIErrorResponse APIErrorResponse;

    public APIRequestException(APIErrorResponse APIErrorResponse) {
        this.APIErrorResponse = APIErrorResponse;
    }

}
