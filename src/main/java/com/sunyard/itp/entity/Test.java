package com.sunyard.itp.entity;

public class Test
{
	private String id;
	private String name;
	private String birthday;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Test(String id, String name, String birthday) {
		super();
		this.id = id;
		this.name = name;
		this.birthday = birthday;
	}
	public Test() {
		super();
	}
	@Override
	public String toString() {
		return "Test [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
	}
	
	
}
