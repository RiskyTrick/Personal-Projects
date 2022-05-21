package com.example.CMS.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Clients")
public class Client {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="first_name",nullable=false)
	private String firstname;
	
	@Column(name="last_name")
	private String lastname;
	
	@Column(name="email",nullable=false)
	private String email;
	
	@Column(name="phone",nullable=false)
	private Long phone;
	
	@Column(name="picture",length=255)
	private String picture;
	
	
	

public Client(){
	
}
public Client(String firstname, String lastname, String email,String picture,Long phone) {
	super();
	this.firstname = firstname;
	this.lastname = lastname;
	this.email = email;
	this.picture=picture;
	this.phone=phone;

}

//----------------------------
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getFirstname() {
	return firstname;
}
public void setFirstname(String firstname) {
	this.firstname = firstname;
}
public String getLastname() {
	return lastname;
}
public void setLastname(String lastname) {
	this.lastname = lastname;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPicture() {
	return picture;
}
public void setPicture(String picture) {
	this.picture = picture;
}
public Long getPhone() {
	return phone;
}
public void setPhone(Long phone) {
	this.phone = phone;
}


//--------------------------------

}