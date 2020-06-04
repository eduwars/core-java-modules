package com.eduwars.corejava.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Ravi Singh
 * @date 04/06/20-06-2020
 * @project eduwars
 */
public enum ErrorCode {

    INTERNAL_ERROR("001", ErrorMessage.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),

    MISSING_ACCESS_TOKEN("002", ErrorMessage.MISSING_ACCESS_TOKEN.getMessage(), HttpStatus.BAD_REQUEST),

    MISSING_TRACE_ID("003", ErrorMessage.MISSING_TRACE_ID.getMessage(), HttpStatus.BAD_REQUEST),

    MISSING_CLIENT_ID("004", ErrorMessage.MISSING_CLIENT_ID.getMessage(), HttpStatus.BAD_REQUEST),

    UNAUTHORIZED_ACCESS_TOKEN("005", ErrorMessage.UNAUTHORIZED_ACCESS_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED),

    BAD_REQUEST_ERROR("006", ErrorMessage.BAD_REQUEST_ERROR.getMessage(), HttpStatus.BAD_REQUEST),

    API_SERVER_ERROR("007", ErrorMessage.API_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),

    TIMEOUT_ERROR("008", ErrorMessage.TIMEOUT_ERROR.getMessage(), HttpStatus.GATEWAY_TIMEOUT);

    @Getter private String code;

    @Getter private String message;

    @Getter private HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


    private enum ErrorMessage {

        NOT_LEVEL_16_TOKEN("requires-level-16-access-token"),

        INVALID_CLUBCARD_NUMBER("clubcard-number-doesn't-match-with-customer-clubcards"),

        TIMEOUT_ERROR("timeout-error"),

        INVALID_BARCODE_AND_CLUBCARD("both-mobile-barcode-and-clubcard-number-can-not-be-null"),

        INVALID_EXPIRY_TIME("invalid-expiry-time"),

        INVALID_DEVICE("invalid-device-type-valid-are-ios-and-android"),

        GOOGLE_SERVICE_UNAVAILABLE("Google-service-is-not-available"),

        UNAUTHORIZED_ACCESS_TOKEN("unauthorized-token"),

        API_SERVER_ERROR("api-internal-server-error"),

        BAD_REQUEST_ERROR("bad-request"),

        INTERNAL_SERVER_ERROR("internal-server-error"),

        MISSING_ACCESS_TOKEN("missing-access-token"),

        MISSING_TRACE_ID("missing-trace-id"),

        MISSING_CLIENT_ID("missing-client-id");

        @Getter
        private String message;

        ErrorMessage(String message) {
            this.message = message;
        }
    }


}