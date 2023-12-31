package com.odin.orchestrator.appmgmt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odin.orchestrator.appmgmt.constants.APIConstants;
import com.odin.orchestrator.appmgmt.constants.ApplicationConstants;
import com.odin.orchestrator.appmgmt.service.APIMetadataService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = ApplicationConstants.API_VERSION)
public class AppConfigController {
	
	@Autowired
	private APIMetadataService apiMetadataService;

	@ApiOperation(value = "API to get urls based on environment", notes = "This API is provides API endpoints")
	@ApiResponses(value = {
	        @ApiResponse(code = 2000, message = ApplicationConstants.SUCCESS_MESSAGE),
	        @ApiResponse(code = 1000, message = ApplicationConstants.FAILURE_MESSAGE)})
	@GetMapping(value = APIConstants.API_INFO)
	public ResponseEntity<Object> apiMetadataController(HttpServletRequest request){
		log.info("Inside API info controller");
		return apiMetadataService.getAPIInfo(request);
	}
}
