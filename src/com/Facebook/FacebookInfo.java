package com.Facebook;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="facebook_info")
public class FacebookInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "facebook_id")
	private String facebook_id;
	@Column(name = "access_token")
	private String access_token;
	
	public FacebookInfo(){
		
	}
	public FacebookInfo(String facebook_id, String access_token) {
		this();
		this.facebook_id = facebook_id;
		this.access_token = access_token;
	}
	public String getFacebook_id() { return facebook_id; }
	public void setFacebook_id(String facebook_id) { this.facebook_id = facebook_id; }
	public String getAccess_token() { return access_token; }
	public void setAccess_token(String access_token) { this.access_token = access_token;}
			
	
}
