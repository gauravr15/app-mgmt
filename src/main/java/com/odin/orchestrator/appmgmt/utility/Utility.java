package com.odin.orchestrator.appmgmt.utility;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odin.orchestrator.appmgmt.constants.ResponseCodes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Utility {

	private final AESHelper aesHelper;

	public Utility(AESHelper aesHelper) {
		this.aesHelper = aesHelper;
	}

	public String getChecksum(HttpServletRequest request) {
		return request.getHeader("checksum");
	}

	public ResponseEntity<Object> validateChecksum(Object requestBody, String reqChecksum) {
		try {
			ResponseObject response = new ResponseObject();

			String requestBodyJSON = Utility.serializeObjectWithSpaces(requestBody);

			String checksum = aesHelper.encrypt(requestBodyJSON);
			if (checksum.equals(reqChecksum)) {
				return new ResponseEntity<>(response.buildResponse(ResponseCodes.SUCCESS), HttpStatus.OK);
			}
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.FAILURE), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while validating checksum: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static String serializeObjectWithSpaces(Object object) {
		try {
			// Serialize the object to a JSON string with pretty printing
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Error occurred while serializing the object to JSON: {}", e.getMessage());
			return null;
		}
	}

	public String getToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	public ResponseEntity<Object> createChecksum(Object requestBody) {
		try {
			ResponseObject response = new ResponseObject();

			String requestBodyJSON = Utility.serializeObjectWithSpaces(requestBody);

			String checksum = aesHelper.encrypt(requestBodyJSON);
			return new ResponseEntity<>(response.buildResponse(checksum), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while validating checksum: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
