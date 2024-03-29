package com.odin.orchestrator.appmgmt.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.odin.orchestrator.appmgmt.constants.ResponseCodes;
import com.odin.orchestrator.appmgmt.entity.APIInfo;
import com.odin.orchestrator.appmgmt.entity.AppVersionModel;
import com.odin.orchestrator.appmgmt.repo.APIInfoRepository;
import com.odin.orchestrator.appmgmt.repo.AppVersionRepository;
import com.odin.orchestrator.appmgmt.service.AppUpdateService;
import com.odin.orchestrator.appmgmt.utility.ResponseObject;
import com.odin.orchestrator.appmgmt.utility.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppUpdateServiceImpl implements AppUpdateService{
	
	@Autowired
	private AppVersionRepository appVersionRepo;
	
	@Autowired
	private APIInfoRepository apiInfoRepo;
	
	@Autowired
	private ResponseObject response;
	
	@Override
	public
	ResponseEntity<Object> checkAppUpdate(HttpServletRequest request){
		
		Double userAppVersion = Utility.getAppVersion(request); 
		String environment = Utility.getEnvironment(request);
		String language = Utility.getLanguage(request);
		if(userAppVersion == 0.0 || ObjectUtils.isEmpty(environment)) {
			log.error("Version or environment missing in request header");
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.APP_VERSION_MISSING), HttpStatus.OK);
		}
		List<AppVersionModel> appVersionModel = appVersionRepo.findFirstByIsMandatoryAndEnvironmentOrderByIdDesc(true, environment);
		AppVersionModel appVersion;
		if(appVersionModel.isEmpty()) 
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.SUCCESS_CODE), HttpStatus.OK);
		else 
			appVersion = appVersionModel.get(0);
		if(Double.parseDouble(appVersion.getVersion()) > userAppVersion) {
			List<String> responseValues = new ArrayList<>();
			responseValues.add(appVersion.getVersion());
			log.info("App needs to be updated from version : {} to {}",userAppVersion, appVersion.getVersion());
			if(ObjectUtils.isEmpty(language))
				return new ResponseEntity<>(response.buildResponse(ResponseCodes.APP_UPDATE_REQUIRED, responseValues), HttpStatus.OK);
			return new ResponseEntity<>(response.buildResponse(language, ResponseCodes.APP_UPDATE_REQUIRED, responseValues), HttpStatus.OK);
		}
		log.info("No app update required");
		List<APIInfo> data = apiInfoRepo.findAllByEnvironmentIgnoreCaseAndAppVersionGreaterThanEqual(environment, String.valueOf(userAppVersion));
		return new ResponseEntity<>(response.buildResponse(ResponseCodes.SUCCESS_CODE, data), HttpStatus.OK);
	}

}
