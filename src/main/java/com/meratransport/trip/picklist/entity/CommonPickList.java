package com.meratransport.trip.picklist.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMMON_PICKLIST")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonPickList {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "COMMON_KEY")
	private String commonKey;

	@Column(name = "COMMON_VALUE")
	private String commonValue;
	
	
	@Column(name = "COMMON_TYPE")
	private String commonType;

}
