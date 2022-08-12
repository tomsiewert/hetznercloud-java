package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum ErrorCode {
    forbidden,
    invalid_input,
    json_error,
    locked,
    not_found,
    rate_limit_exceeded,
    resource_limit_exceeded,
    resource_unavailable,
    service_error,
    uniqueness_error,
    @JsonAlias("protected")
    _protected,
    maintenance,
    conflict,
    unsupported_error,
    token_readonly
}
