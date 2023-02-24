package com.smartContactmanager.Helpers;

public class MessageHelper {

	private String content;
	private String type;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MessageHelper(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
}
