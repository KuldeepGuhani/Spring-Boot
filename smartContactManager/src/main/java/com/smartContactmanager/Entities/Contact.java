package com.smartContactmanager.Entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	private String name;
	private String secondName;
	private String phone;
	private String discription;
	private String work;
	private String emailId;
	private String Image;
	
	@ManyToOne
	private User user;
	public int getCid() {
		return cid;
	}

	@Override
	public boolean equals(Object obj) {
		
		return this.cid == ((Contact) obj).getCid();
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Contact(int cid, String name, String secondName, String phone, String discription, String work,
			String emailId, String image, User user) {
		super();
		this.cid = cid;
		this.name = name;
		this.secondName = secondName;
		this.phone = phone;
		this.discription = discription;
		this.work = work;
		this.emailId = emailId;
		this.Image = image;
		this.user = user;
	}
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
//	@Override
//	public String toString() {
//		return "Contact [cid=" + cid + ", name=" + name + ", secondName=" + secondName + ", phone=" + phone
//				+ ", discription=" + discription + ", work=" + work + ", emailId=" + emailId + ", Image=" + Image
//				+ ", user=" + user + "]";
//	}
	
}
