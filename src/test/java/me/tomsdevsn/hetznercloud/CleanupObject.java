package me.tomsdevsn.hetznercloud;

public class CleanupObject {
    private final long id;
    private final CleanupType cleanupType;

    public CleanupObject(long id, CleanupType cleanupType) {
        this.id = id;
        this.cleanupType = cleanupType;
    }

    public long getId() {
        return id;
    }

    public CleanupType getCleanupType() {
        return cleanupType;
    }

    @Override
    public String toString() {
        return "CleanupObject{" +
                "id=" + id +
                ", cleanupType=" + cleanupType +
                '}';
    }
}
