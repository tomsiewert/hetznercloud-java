package me.tomsdevsn.hetznercloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class CleanupObject {
    private final long id;
    private final CleanupType cleanupType;

}
