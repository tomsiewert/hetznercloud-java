package me.tomsdevsn.hetznercloud;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UrlBuilder {

    private String url;

    private Map<String, String> queryParams = new HashMap<>();

    private UrlBuilder(String url) {
        this.url = url;
    }

    public static UrlBuilder from(String url) {

        if (url == null || url.isBlank()) {
            throw new RuntimeException("url must not be empty or blank");
        }

        return new UrlBuilder(url);
    }

    public UrlBuilder queryParamIfPresent(String name, Optional<Integer> value) {
        value.ifPresent((it) -> queryParams.put(name, it.toString()));
        return this;

    }

    public String toUri() {

        if (queryParams.isEmpty()) {
            return url;
        }

        return url + "?" + queryParams.entrySet().stream().map((it) -> it.getKey() + "=" + it.getValue()).collect(Collectors.joining("&"));
    }

    public UrlBuilder queryParam(String name, Object value) {
        queryParams.put(name, Objects.toString(value));
        return this;
    }
}
