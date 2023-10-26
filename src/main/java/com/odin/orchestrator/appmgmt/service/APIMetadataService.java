package com.odin.orchestrator.appmgmt.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

public interface APIMetadataService {
	
	ResponseEntity<Object> getAPIInfo(HttpServletRequest request);
}
