package com.Madeleine.Entity;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.OrderBy;

import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import com.JSON.ExcludeValue;

@Entity
@Table(name="Madeleine")
public class Madeleine implements Serializable {
	public enum MadeleineSendState{
		NOT_SENDED,
		SENDED,
		OPENED
	};
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_TRIGGER")
	@GenericGenerator(name ="SEQ_TRIGGER",strategy="jpl.hibernate.util.TriggerAssignedIdentityGenerator")
	@Column(name = "idx")
	private Integer idx;
	@Column(name = "name")
	private String name;
	@Column(name = "created_time")
	private Timestamp createdTime;
	@Column(name = "reserved_time")
	private Timestamp reservedTime;
	@Column(name = "send_state")
	private MadeleineSendState sendState;
	@Column(name = "is_public")
	private boolean	isPublic; 
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private User user;
	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}
	
	@OneToMany(mappedBy = "madeleine",fetch = FetchType.LAZY)
	@OrderBy("idx")
	private Set<MadeleinePhoto> photos;
	public Set<MadeleinePhoto> getPhotos() {return photos;}
	public void setPhotos(Set<MadeleinePhoto> photos) {this.photos = photos;}
	public void addPhoto(MadeleinePhoto photo){
		photo.setMadeleine(this);
		this.getPhotos().add(photo);
	}
	
	public Madeleine(){
	}
	public Madeleine(Integer idx, String name, Timestamp createdTime,
			Timestamp reservedTime, MadeleineSendState sendState,
			boolean isPublic, User user, Set<MadeleinePhoto> photos) {
		super();
		this.idx = idx;
		this.name = name;
		this.createdTime = createdTime;
		this.reservedTime = reservedTime;
		this.sendState = sendState;
		this.isPublic = isPublic;
		this.user = user;
		this.photos = photos;
	}
	
	public Integer getIdx() {return idx;}
	public void setIdx(Integer idx) {this.idx = idx;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public Timestamp getCreatedTime() {return createdTime;}
	public void setCreatedTime(Timestamp createdTime) {this.createdTime = createdTime;}
	public Timestamp getReservedTime() {return reservedTime;}
	public void setReservedTime(Timestamp reservedTime) {this.reservedTime = reservedTime;}
	public MadeleineSendState getSendState() {return sendState;}
	public void setSendState(MadeleineSendState sendState) {this.sendState = sendState;}
	public boolean getIsPublic() {return isPublic;}
	public void setIsPublic(boolean isPublic) {this.isPublic = isPublic;}
	
	
	public void hibernateInitialize(){
		Hibernate.initialize(this);
		Hibernate.initialize(this.getUser());
		Hibernate.initialize(this.getPhotos());
		if(this.getPhotos()!=null)
			for(MadeleinePhoto photo : this.getPhotos())
				Hibernate.initialize(photo);
	}
}
