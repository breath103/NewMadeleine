package com.Madeleine.Entity;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;


@Entity
@Table(name="Madeleine_Photo")
public class MadeleinePhoto {
	private static final long serialVersionUID = 1L;
	public enum SOURCE_TYPE{
		FACEBOOK,
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_TRIGGER")
	@GenericGenerator(name ="SEQ_TRIGGER",strategy="jpl.hibernate.util.TriggerAssignedIdentityGenerator")
	@Column(name = "idx")
	private Integer idx;
	@Column(name = "src")
	private String src;
	@Column(name = "description")
	private String description;
	@Column(name = "source_type")
	private SOURCE_TYPE sourceType;
	@Column(name = "source_id")
	private String sourceID;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "madeleine_idx")
	private Madeleine madeleine;
	
	public Madeleine getMadeleine() {return madeleine;}
	public void setMadeleine(Madeleine madeleine) {	this.madeleine = madeleine;}
	public SOURCE_TYPE getSourceType() {return sourceType;}
	public void setSourceType(SOURCE_TYPE sourceType) {this.sourceType = sourceType;}
	public String getSourceID() { return sourceID; }
	public void setSourceID(String sourceID) { this.sourceID = sourceID; }
	public Integer getIdx() { return idx;}
	public void setIdx(Integer idx) {this.idx = idx;}
	public String getSrc() { return src;}
	public void setSrc(String src) {this.src = src;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
}
