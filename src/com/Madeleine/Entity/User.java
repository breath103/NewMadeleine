package com.Madeleine.Entity;

import java.util.List;
import java.util.Set;
import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;

import com.Madeleine.Entity.Madeleine.MadeleineSendState;

@Entity
@Table(
		name="Madeleine_User"
)
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String sessionAttributeName = "LogiendUser";
	public enum USER_TYPE{
		FACEBOOK_USER,
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_TRIGGER")
	@GenericGenerator(name ="SEQ_TRIGGER",strategy="jpl.hibernate.util.TriggerAssignedIdentityGenerator")
	@Column(name = "idx")
	private Integer idx;
	@Column(name = "user_type")
	private USER_TYPE userType;
	@Column(name = "name")
	private String name;
	@Column(name = "email",unique = true)
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "foreign_user_id")
	private String foreginUserId;
	@Column(name = "foreign_access_token")
	private String foreginAccessToken;

	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private Set<Madeleine> madeleineList;
	
	public User(){
	}
	public User(USER_TYPE userType, String name, String email,
			String password, String foreginUserId, String foreginAccessToken) {
		this();
		this.userType = userType;
		this.name = name;
		this.email = email;
		this.password = password;
		this.foreginUserId = foreginUserId;
		this.foreginAccessToken = foreginAccessToken;
	}
	

	public Set<Madeleine> getMadeleineList() {
		return madeleineList;
	}
	public void setMadeleineList(Set<Madeleine> madeleineList) {
		this.madeleineList = madeleineList;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public USER_TYPE getUserType() {
		return userType;
	}
	public void setUserType(USER_TYPE userType) {
		this.userType = userType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getForeginUserId() {
		return foreginUserId;
	}
	public void setForeginUserId(String foreginUserId) {
		this.foreginUserId = foreginUserId;
	}
	public String getForeginAccessToken() {
		return foreginAccessToken;
	}
	public void setForeginAccessToken(String foreginAccessToken) {
		this.foreginAccessToken = foreginAccessToken;
	}
	
	public List<Madeleine> getMadeleineInState(Session session,MadeleineSendState sendState){
		return (List<Madeleine>)
				session.createQuery("from Madeleine where user = ? and sendState = ?").setParameter(0, this)
																					 .setParameter(1, sendState).list();
	}
	
	public static User generateWithFacebookInfo(String facebook_id,String access_token,String name,String email){
		User user = new User();
		user.setUserType(USER_TYPE.FACEBOOK_USER);
		user.setEmail(email);
		user.setForeginUserId(facebook_id);
		user.setForeginAccessToken(access_token);
		user.setName(name);
		return user;
	}
	
	public void hibernateInitialize(){
		Hibernate.initialize(this);
		Hibernate.initialize(this.getMadeleineList());
	}
	
}

