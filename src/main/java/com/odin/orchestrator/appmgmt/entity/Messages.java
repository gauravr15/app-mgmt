package com.odin.orchestrator.appmgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "response_messages")
@NoArgsConstructor
@AllArgsConstructor
public class Messages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	Integer Id;
	
	@Column(name = "message_en")
	String messageEn;
	
	@Column(name = "message_ch")
	String messageCh;
	
	@Column(name = "message_hi")
	String messageHi;
	
	@Column(name = "message_sp")
	String messageSp;
	
	@Column(name = "message_fr")
	String messageFr;
	
	@Column(name = "message_ar")
	String messageAr;
	
	@Column(name = "message_bg")
	String messageBg;
	
	@Column(name = "message_pg")
	String messagePg;
	
	@Column(name = "message_rs")
	String messageRs;
	
	@Column(name = "message_ur")
	String messageUr;
}
