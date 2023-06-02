package com.nale.newslettermanagement.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Bulletin {
	
	private UUID id;
	private String name;
	private String description;
	private Set<UUID> followersIds = new HashSet<UUID>();
	
	public Bulletin(UUID id) {
		this.id = id;
		this.name = "";
		this.description = "";
	}
	
	public Bulletin(UUID id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

    public Bulletin() {
    }

    public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public Set<UUID> getFollowersIds() {
		return followersIds;
	}

	public void setFollowersIds(Set<UUID> followersIds) {
		this.followersIds = followersIds;
	}

	@Override
	public String toString() {
		return "Bulletin{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", followersIds=" + followersIds +
				'}';
	}
}
