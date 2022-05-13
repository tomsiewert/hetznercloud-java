package me.tomsdevsn.hetznercloud.objects.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {

    SYSTEM("system"),
    SNAPSHOT("snapshot"),
    BACKUP("backup"),
    APP("app");

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
