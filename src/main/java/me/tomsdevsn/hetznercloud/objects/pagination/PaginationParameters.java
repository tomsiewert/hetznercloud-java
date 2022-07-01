package me.tomsdevsn.hetznercloud.objects.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationParameters {
    public Integer page;
    public Integer perPage;
}
