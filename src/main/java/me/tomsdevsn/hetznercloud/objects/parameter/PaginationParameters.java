package me.tomsdevsn.hetznercloud.objects.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationParameters {
    private Integer page;
    private Integer perPage;

    public static PaginationParameters of(Integer page, Integer perPage) {
        return new PaginationParameters(page, perPage);
    }

    public static PaginationParameters of(Integer page) {
        return new PaginationParameters(page, null);
    }

    public static PaginationParameters empty() {
        return new PaginationParameters(null, null);
    }
}
