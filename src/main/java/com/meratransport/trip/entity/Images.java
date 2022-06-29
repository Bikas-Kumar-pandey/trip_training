package com.meratransport.trip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
@Entity
public class Images extends BaseEntity {	

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	private String type;

	@Column(columnDefinition="TEXT")
	private String imgLink;
	
	
	@Column(columnDefinition="TEXT")
	private String imgLinkDestination;

	private boolean isDamaged = false;
	
	private String referenceId;
	
	private String notes;
	
	private String reason;
	
}
