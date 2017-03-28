package com.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author harold.murcia
 *
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
