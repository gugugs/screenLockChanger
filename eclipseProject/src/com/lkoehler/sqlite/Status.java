package com.lkoehler.sqlite;

public class Status {

	private long id;

	private String name;

	private int status;

	public Status(long id, String name, int status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
