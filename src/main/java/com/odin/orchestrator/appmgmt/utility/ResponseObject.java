package com.odin.orchestrator.appmgmt.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.odin.orchestrator.appmgmt.constants.LanguageConstants;
import com.odin.orchestrator.appmgmt.constants.ResponseCodes;
import com.odin.orchestrator.appmgmt.entity.Messages;
import com.odin.orchestrator.appmgmt.repo.MessagesRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Component
@JsonInclude(Include.NON_NULL)
public class ResponseObject {

    @Autowired
    private MessagesRepository messageRepo;

    private Integer statusCode;
    private String status;
    private String message;
    private Object data;
    private String language;

    public ResponseObject() {
        this.statusCode = ResponseCodes.SUCCESS_CODE;
        this.status = ResponseCodes.SUCCESS;
    }

    public ResponseObject buildResponse(String lang, Integer statusCode) {
        if (statusCode == null) {
            statusCode = ResponseCodes.SUCCESS_CODE;
        }
        if (ObjectUtils.isEmpty(lang)) {
            lang = "en";
        }

        ResponseObject response = ResponseObject.builder().statusCode(statusCode).language(lang).build();

        if (statusCode >= ResponseCodes.SUCCESS_CODE) {
            response.setStatus(ResponseCodes.SUCCESS);
        } else {
            response.setStatus(ResponseCodes.FAILURE);
        }

        Messages msg = messageRepo.findById(statusCode).orElse(null);
        if (msg == null) {
            response.setMessage(lang.equals(LanguageConstants.EN) ? ResponseCodes.SUCCESS : ResponseCodes.FAILURE);
        } else {
            response.setMessage(getLanguageBasedMessage(msg, lang));
        }

        return response;
    }

    public ResponseObject buildResponse(String lang, Integer statusCode, Object data) {
        ResponseObject response = buildResponse(lang, statusCode);
        response.setData(data);
        return response;
    }

    public ResponseObject buildResponse(Object data) {
        ResponseObject response = ResponseObject.builder().status(ResponseCodes.SUCCESS).statusCode(ResponseCodes.SUCCESS_CODE).build();
        response.setData(data);
        return response;
    }

    private String getLanguageBasedMessage(Messages finalMessage, String lang) {
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
            default:
                return ResponseCodes.SUCCESS;
        }
    }
}
