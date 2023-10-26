package com.odin.orchestrator.appmgmt.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.odin.orchestrator.appmgmt.constants.ApplicationConstants;
import com.odin.orchestrator.appmgmt.constants.ResponseCodes;
import com.odin.orchestrator.appmgmt.entity.APIInfo;
import com.odin.orchestrator.appmgmt.repo.APIInfoRepository;
import com.odin.orchestrator.appmgmt.service.APIMetadataService;
import com.odin.orchestrator.appmgmt.utility.ResponseObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class APIMetadataServiceImpl implements APIMetadataService{

	@Autowired
	private APIInfoRepository apiInfoRepo;
	
	@Override
	public ResponseEntity<Object> getAPIInfo(HttpServletRequest request){
		ResponseObject response = new ResponseObject();
		try {
		String env = request.getHeader(ApplicationConstants.ENVIRONMENT);
		String requestId = request.getHeader(ApplicationConstants.REQUEST_ID);
		if(ObjectUtils.isEmpty(env)) {
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.INVALID_REQUEST), HttpStatus.OK);
		}
		log.info("Fetching API details for {} environmant for request ID :{}", env, requestId);
		List<APIInfo> apiInfo = apiInfoRepo.findByEnvironmentIgnoreCase(env);
		if(apiInfo.isEmpty()) 
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.NO_DATA_FOUND), HttpStatus.OK);
		else
			return new ResponseEntity<>(response.buildResponse(apiInfo), HttpStatus.OK);
		}
		catch(Exception e) {
			log.error("Error occured while fetching api info : {}",e.getMessage());
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.INTERNAL_SERVER_ERROR), HttpStatus.OK);
		}
	}
}
