package me.tomsdevsn.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    private Pagination pagination;

    @Getter
    @Setter
    public static class Pagination {

        private long page;
        @JsonProperty("per_page")
        private long perPage;
        @JsonProperty("previous_page")
        private long previousPage;
        @JsonProperty("next_page")
        private long nextPage;
        @JsonProperty("last_page")
        private long lastPage;
        @JsonProperty("total_entries")
        private long totalEntries;
    }
}