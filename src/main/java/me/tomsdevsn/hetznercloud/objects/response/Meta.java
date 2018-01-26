package me.tomsdevsn.hetznercloud.objects.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    private Pagination pagination;

    @Getter
    @Setter
    public static class Pagination {

        private Integer page;
        private Integer perPage;
        private Integer previousPage;
        private Integer nextPage;
        private Integer lastPage;
        private Integer totalEntries;
    }
}