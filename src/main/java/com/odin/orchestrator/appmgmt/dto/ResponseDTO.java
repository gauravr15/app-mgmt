package com.odin.orchestrator.appmgmt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(Include.NON_NULL)
public class ResponseDTO {
	
	private Integer statusCode;
    private String status;
    private String message;
    private Object data;
}
