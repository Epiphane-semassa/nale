package com.nale.newslettermanagement.data;

import java.io.File;

public class StorageProperties {
	
	private String location;
    private static StorageProperties storageProperties;

    private StorageProperties() {
		this.location = System.getProperty("user.home") + File.separator + "Nale";	
	}

	public static StorageProperties getInstance() {
    	if(storageProperties==null)
    		storageProperties = new StorageProperties();

    	return storageProperties;
	}

	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
