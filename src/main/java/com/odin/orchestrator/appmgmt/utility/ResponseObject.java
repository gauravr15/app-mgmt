package com.odin.orchestrator.appmgmt.utility;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.odin.orchestrator.appmgmt.constants.LanguageConstants;
import com.odin.orchestrator.appmgmt.constants.ResponseCodes;
import com.odin.orchestrator.appmgmt.dto.ResponseDTO;
import com.odin.orchestrator.appmgmt.entity.ResponseMessages;
import com.odin.orchestrator.appmgmt.repo.ResponseMessagesRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonInclude(Include.NON_NULL)
public class ResponseObject {
    
    @Autowired
    private ResponseMessagesRepository responseMessageRepo;

    public ResponseDTO buildResponse() {
    	return ResponseDTO.builder().statusCode(ResponseCodes.SUCCESS_CODE).status(ResponseCodes.SUCCESS).build();
    }

    public ResponseDTO buildResponse(String lang, Integer statusCode) {
        if (statusCode == null) {
            statusCode = ResponseCodes.SUCCESS_CODE;
        }
        if (ObjectUtils.isEmpty(lang)) {
            lang = "en";
        }

        ResponseDTO response = ResponseDTO.builder().statusCode(statusCode).build();

        if (statusCode >= ResponseCodes.SUCCESS_CODE) {
            response.setStatus(ResponseCodes.SUCCESS);
        } else {
            response.setStatus(ResponseCodes.FAILURE);
        }

        Optional<ResponseMessages> msg = responseMessageRepo.findById(statusCode);
        if (!msg.isPresent()) {
            response.setMessage(lang.equals(LanguageConstants.EN) ? ResponseCodes.SUCCESS : ResponseCodes.FAILURE);
        } else {
            response.setMessage(getLanguageBasedMessage(msg.get(), lang));
        }
        return response;
    }

    public ResponseDTO buildResponse(String lang, Integer statusCode, Object data) {
    	ResponseDTO response = buildResponse(lang, statusCode);
    	int i = 0;
		String text = response.getMessage();
		List<?> values = (List<?>) data;

		while (text.contains("%s") && i < values.size()) {
			text = text.replaceFirst("%s", String.valueOf(values.get(i)));
			i++;
		}

		response.setMessage(text);
        return response;
    }

    public ResponseDTO buildResponse(Object data) {
    	ResponseDTO response = ResponseDTO.builder().status(ResponseCodes.SUCCESS).statusCode(ResponseCodes.SUCCESS_CODE).build();
        response.setData(data);
        return response;
    }
    
    public ResponseDTO buildResponse(Integer statusCode) {
    	ResponseDTO response = ResponseDTO.builder().statusCode(statusCode).build();
        if(statusCode >= ResponseCodes.SUCCESS_CODE)
        	response.setStatus(ResponseCodes.SUCCESS);
        else
        	response.setStatus(ResponseCodes.FAILURE);
        return response;

    }
    
    public ResponseDTO buildResponse(Integer statusCode, Object data) {

    	ResponseDTO response = ResponseDTO.builder().statusCode(statusCode).build();
		try {
			Optional<ResponseMessages> responseMessage = responseMessageRepo.findById(statusCode);
			if (!ObjectUtils.isEmpty(responseMessage)) {
				int i = 0;
				String text = responseMessage.get().getMessageEn();
				List<?> values = (List<?>) data;

				while (text.contains("%s") && i < values.size()) {
					text = text.replaceFirst("%s", String.valueOf(values.get(i)));
					i++;
				}

				response.setMessage(text);
			} else {
				// Handle the case where no message is found for the statusCode
				response.setMessage("Default message for status code: " + statusCode);
			}

			response.setStatus(
					statusCode >= ResponseCodes.SUCCESS_CODE ? ResponseCodes.SUCCESS : ResponseCodes.FAILURE);
			response.setData(data);
			return response;
		} catch (Exception e) {
			log.error("Error occurred: {}", e.getMessage());
			// Handle the exception gracefully
			response.setStatus(
					statusCode >= ResponseCodes.SUCCESS_CODE ? ResponseCodes.SUCCESS : ResponseCodes.FAILURE);
			return response;
		}
	}
    
    public String getLanguageBasedMessage(ResponseMessages finalMessage, String lang) {
        switch (lang) {
            case LanguageConstants.EN:
                return finalMessage.getMessageEn();
            case LanguageConstants.CH:
                return finalMessage.getMessageCh();
            case LanguageConstants.HI:
                return finalMessage.getMessageHi();
            case LanguageConstants.SP:
                return finalMessage.getMessageSp();
            case LanguageConstants.FR:
                return finalMessage.getMessageFr();
            case LanguageConstants.AR:
                return finalMessage.getMessageAr();
            case LanguageConstants.BG:
                return finalMessage.getMessageBg();
            case LanguageConstants.PG:
                return finalMessage.getMessagePg();
            case LanguageConstants.UR:
                return finalMessage.getMessageUr();
            case LanguageConstants.RS:
                return finalMessage.getMessageRs();
            default:
                return ResponseCodes.SUCCESS;
        }
    }
}
