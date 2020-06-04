package com.eduwars.corejava.common.connector;

import com.eduwars.corejava.common.Constants.Constants;
import com.eduwars.corejava.common.error.ErrorCode;
import com.eduwars.corejava.common.exception.CustomException;
import com.eduwars.corejava.common.logger.CustomLogger;
import com.eduwars.corejava.common.logger.LoggerConstants;
import com.eduwars.corejava.common.logger.LoggerContext;
import com.eduwars.corejava.common.utils.CommonUtils;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;

/**
 * @author Ravi Singh
 * @date 03/06/20-06-2020
 * @project eduwars
 */
@Component
public final class ClassicConnector {
    @Autowired
    private WebClient webClient;

    @Getter
    @Setter
    private boolean ignoreHttpError;

    private boolean validate(ConnectorRequestBody connectorRequestBody) {
        return true;
    }

    private <T extends Object> long getContentLength(T content) {
        String json = content instanceof String ? (String) content : new Gson().toJson(content);
        return json.getBytes(StandardCharsets.UTF_8).length;
    }

    private Mono<ResponseEntity<ClassicResponse>> makeApiCall(ConnectorRequestBody connectorRequestBody) {
        final long startTime = System.currentTimeMillis();

        /* Adding content Length */
        if(StringUtils.isEmpty(Long.toString(connectorRequestBody.getHeaders().getContentLength())) && connectorRequestBody.getBody() != null) {
            connectorRequestBody.getHeaders().setContentLength(getContentLength(connectorRequestBody.getBody()));
        }



        /* API Call Started */
        LoggerContext loggerContext= LoggerContext.builder()
                .add(LoggerConstants.KEYS.HTTPREQUESMETHOD, connectorRequestBody.getMethod().name())
                .add(LoggerConstants.KEYS.REQUESTURI, connectorRequestBody.getUri().toString())
                .add(LoggerConstants.KEYS.TRACEID, connectorRequestBody.getHeaders().get(LoggerConstants.KEYS.TRACEID.value()).toString())
                .add(LoggerConstants.KEYS.STATE, connectorRequestBody.getState().started());
        CustomLogger.outgoingApiCall(loggerContext, Constants.API_CALL_STARTED);

        return webClient.method(connectorRequestBody.getMethod())
                .uri(connectorRequestBody.getUri())
                .headers(h -> updateHeaders(connectorRequestBody.getHeaders(), h))
                .exchange()
                .flatMap(r -> r.toEntity(ClassicResponse.class))
                .doOnSuccess(r -> {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    long responseSize = getContentLength(r.getBody());
                    loggerContext.add(LoggerConstants.KEYS.TIME_TAKEN, CommonUtils.getTimeInString(timeTaken))
                            .add(LoggerConstants.KEYS.HTTPSTATUSCODE, String.valueOf(r.getStatusCode().value()))
                            .add(LoggerConstants.KEYS.STATE, connectorRequestBody.getState().finished())
                            .add(LoggerConstants.KEYS.RESPONSE_SIZE, CommonUtils.getSizeInString(responseSize));
                    CustomLogger.incomingApiCall(loggerContext, Constants.API_CALL_ENDED);
                    if(!isIgnoreHttpError()) {
                        this.handle4XXOr5XXError(r);
                    }
                })
                .doOnError( err -> {
                    CustomException error = CustomException.wrap(err);
                    loggerContext.add(LoggerConstants.KEYS.ERRORCODE, error.getErrorCode().getCode())
                            .add(LoggerConstants.KEYS.ERRORMESSAGE, error.getErrorCode().getMessage())
                            .add(LoggerConstants.KEYS.HTTPSTATUSCODE, String.valueOf(error.getErrorCode().getHttpStatus().value()))
                            .add(LoggerConstants.KEYS.STATE, connectorRequestBody.getState().failed());
                    CustomLogger.incomingApiCall(loggerContext, Constants.API_CALL_FAILED);
                });

    }


    private void updateHeaders(HttpHeaders headers, HttpHeaders h) {
        if (MapUtils.isNotEmpty(headers)) {
            h.addAll(headers);
        }
    }

    private void handle4XXOr5XXError(ResponseEntity<ClassicResponse> response) {
        int status = response.getStatusCodeValue();

        switch (status){
            case 400:
                throw new CustomException(ErrorCode.BAD_REQUEST_ERROR);
            case 401:
                throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
            case 500:
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            case 504:
                throw new CustomException(ErrorCode.TIMEOUT_ERROR);
        }
    }

    /**
     * make api call using spring async web client.
     *
     * @param connectorRequestBody
     * @return
     */
    public Mono<ResponseEntity<ClassicResponse>> makeCall(ConnectorRequestBody connectorRequestBody) {
        return  Mono.just(connectorRequestBody)
                .flatMap((body) -> {
                    if(!this.validate(body)) {
                        Mono.error(new CustomException(ErrorCode.INTERNAL_ERROR));
                    }
                    return Mono.just(body);
                })
                .flatMap(this::makeApiCall).publishOn(Schedulers.parallel());
    }

}