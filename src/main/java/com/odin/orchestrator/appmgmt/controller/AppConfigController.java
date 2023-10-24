package com.odin.orchestrator.appmgmt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odin.orchestrator.appmgmt.constants.APIConstants;
import com.odin.orchestrator.appmgmt.constants.ApplicationConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = ApplicationConstants.API_VERSION)
public class AppConfigController {
	
	@ApiOperation(value = "app update check api", notes = "This API is to check app updates")
	@ApiResponses(value = {
	        @ApiResponse(code = 2000, message = ApplicationConstants.SUCCESS_MESSAGE),
	        @ApiResponse(code = 1000, message = ApplicationConstants.FAILURE_MESSAGE)})
	@GetMapping(value = APIConstants.CHECK_APP_VERSION)
	public ResponseEntity<Object> appUpdateController(HttpServletRequest request){
		log.info("Inside App version controller");
		return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
	}

}
