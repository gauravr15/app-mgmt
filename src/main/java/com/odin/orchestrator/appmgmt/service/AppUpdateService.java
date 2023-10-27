package com.odin.orchestrator.appmgmt.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

public interface AppUpdateService {

	ResponseEntity<Object> checkAppUpdate(HttpServletRequest request);

}
