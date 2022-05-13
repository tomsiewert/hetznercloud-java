package me.tomsdevsn.hetznercloud.objects.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class PaginationParameters {
    public Integer page;
    public Integer perPage;
}
