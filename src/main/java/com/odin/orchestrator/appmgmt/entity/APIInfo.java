package com.odin.orchestrator.appmgmt.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="API_INFO")
public class APIInfo {

	@Id
	@Column(name= "ID")
	private Long id;
	
	@Column(name= "ENVIRONMENT")
	private String environment;
	
	@Column(name= "ACTION")
	private String action;
	
	@Column(name= "MODULE")
	private String module;
	
	@Column(name= "IS_AUTH_REQUIRED")
	private Boolean isAuthRequired;
	
	@Column(name= "URI")
	private String uri;
	
	@CreationTimestamp
	@Column(name = "CREATED_TIMESTAMP")
	private Timestamp createdTime;
	
	@UpdateTimestamp
	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTime;
}
