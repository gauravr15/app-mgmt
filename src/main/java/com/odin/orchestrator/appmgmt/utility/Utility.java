package com.odin.orchestrator.appmgmt.utility;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odin.orchestrator.appmgmt.constants.ResponseCodes;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Utility {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.validity}")
	private Long expirationMillis;

	@Autowired
	private ResponseObject response;
	
	private final ChecksumHelper aesHelper;

	public Utility(ChecksumHelper aesHelper) {
		this.aesHelper = aesHelper;
	}

	public String getChecksum(HttpServletRequest request) {
		return request.getHeader("checksum");
	}

	public ResponseEntity<Object> validateChecksum(Object requestBody, String reqChecksum) {
		try {

			String requestBodyJSON = Utility.serializeObjectWithSpaces(requestBody);

			String checksum = aesHelper.encrypt(requestBodyJSON);
			if (checksum.equals(reqChecksum)) {
				return new ResponseEntity<>(response.buildResponse(ResponseCodes.SUCCESS_CODE), HttpStatus.OK);
			}
			return new ResponseEntity<>(response.buildResponse(ResponseCodes.FAILURE_CODE), HttpStatus.OK);
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

			String requestBodyJSON = Utility.serializeObjectWithSpaces(requestBody);

			String checksum = aesHelper.encrypt(requestBodyJSON);
			return new ResponseEntity<>(response.buildResponse(checksum), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while validating checksum: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> generateToken(String userId) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationMillis);

		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

		JSONObject subObject = new JSONObject();
		subObject.put("userId", userId);

		String token = Jwts.builder().setSubject(subObject.toString()).setIssuer(issuer).setIssuedAt(now)
				.setExpiration(expiration).signWith(key, SignatureAlgorithm.HS256).compact();
		return new ResponseEntity<>(response.buildResponse(token), HttpStatus.OK);
	}

	public static Double getAppVersion(HttpServletRequest request) {
		return Double.valueOf(
				ObjectUtils.isEmpty(request.getHeader("appVersion")) ? "0.0" : request.getHeader("appVersion"));
	}
	
	public static String getEnvironment(HttpServletRequest req) {
		return req.getHeader("env");
	}
	
	public static String getLanguage(HttpServletRequest req) {
		return req.getHeader("lang");
	}
}
