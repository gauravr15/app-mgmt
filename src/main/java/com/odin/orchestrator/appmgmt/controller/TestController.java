package com.odin.orchestrator.appmgmt.controller;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odin.orchestrator.appmgmt.constants.ApplicationConstants;
import com.odin.orchestrator.appmgmt.dto.TestDTO;
import com.odin.orchestrator.appmgmt.utility.ResponseObject;
import com.odin.orchestrator.appmgmt.utility.Utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(ApplicationConstants.API_VERSION)
public class TestController {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.issuer}")
	private String jwtIssuer;

	@Value("${jwt.validity}")
	private Long jwtValidityMilli;

	@Autowired
	private Utility utility;

	@PostMapping(value = "/createToken")
	public ResponseEntity<Object> getToken(HttpServletRequest req, @RequestBody String userId) {
		ResponseObject response = new ResponseObject();
		String secretKeyString = jwtSecret;
		byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyString);
		SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
		String subject = userId;
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + jwtValidityMilli);
		String issuer = jwtIssuer;
		String jwtToken = Jwts.builder().setSubject(subject).setIssuedAt(issuedAt).setExpiration(expiration)
				.setIssuer(issuer).signWith(secretKey).compact();
		return new ResponseEntity<>(response.buildResponse(jwtToken), HttpStatus.OK);
	}

	@PostMapping("/createChecksum")
	public ResponseEntity<Object> createChecksum(@RequestBody TestDTO requestBody) {
		try {
			return utility.createChecksum(requestBody);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/getAuthToken")
	public ResponseEntity<Object> generateAuthToken(HttpServletRequest req) {
		try {
			return utility.generateToken(req.getHeader("userId"));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/validateChecksum")
	public ResponseEntity<Object> validateChecksum(HttpServletRequest req, @RequestBody TestDTO requestBody) {
		try {
			return utility.validateChecksum(requestBody, utility.getChecksum(req));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
