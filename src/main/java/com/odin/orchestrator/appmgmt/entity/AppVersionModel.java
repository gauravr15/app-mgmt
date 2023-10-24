package com.odin.orchestrator.appmgmt.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "APP_VERSION")
@Getter
@Setter
public class AppVersionModel {
	
	@Id
	private Long id;
	
	@Column(name = "ENVIRONMENT")
	private String env;

	@Column(name = "VERSION")
	private String version;
	
	@Column(name = "IS_USABLE")
	private boolean isUseable;
	
	@CreationTimestamp
	@Column(name = "CREATION_TIMESTAMP")
	private Timestamp creationTime;
	
	@CreationTimestamp
	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTime;
}
