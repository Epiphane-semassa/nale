package com.nale.newslettermanagement.model;

import java.util.UUID;

public class Message {
	
	private UUID id;
	private String content;
	private long peopleTouched = 0;
	private UUID bulletinId;
	
	public Message(UUID id) {
		this.id = id;
	}

	public Message(UUID id, String content, UUID bulletinId) {
		super();
		this.id = id;
		this.content = content;
		this.bulletinId = bulletinId;
	}

    public Message() {

    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UUID getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(UUID bulletinId) {
		this.bulletinId = bulletinId;
	}

	public long getPeopleTouched() {
		return peopleTouched;
	}

	public void setPeopleTouched(long peopleTouched) {
		this.peopleTouched = peopleTouched;
	}
}
