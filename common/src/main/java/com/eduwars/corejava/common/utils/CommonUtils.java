package com.eduwars.corejava.common.utils;

import com.eduwars.corejava.common.exception.CustomException;
import com.eduwars.corejava.common.logger.LoggerConstants;
import com.eduwars.corejava.common.models.requests.Request;
import com.eduwars.corejava.common.models.streams.Streams;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

/**
 * @author Ravi Singh
 * @date 04/06/20-06-2020
 * @project eduwars
 */
public class CommonUtils {

    /**
     * return time in ms or sec
     *
     * @param time
     * @return
     */
    public static String getTimeInString(long time) {
        double t = time/1000.0;
        return String.format("%ss",String.valueOf(t));
    }


    /**
     * return body size in kb of bytes
     *
     * @param size
     * @return
     */
    public static String getSizeInString(long size) {
        if(size > 1000) {
            return String.format("%sKB", String.valueOf(size/1000.0));
        }
        return String.format("%sB", String.valueOf(size));
    }




    public static <T1 extends Streams, T2 extends Request>  T2 createRequestBody(T1 stream, T2 requestbody) {

        /* create headers */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(stream.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Accept-Language", "application/json");
        httpHeaders.set(LoggerConstants.KEYS.TRACEID.value(), stream.getTraceId());

        /* create uri builder */
        UriComponentsBuilder url =
                UriComponentsBuilder.fromHttpUrl(requestbody.getUrl());

        requestbody.setHeaders(httpHeaders);
        requestbody.setUriBuilder(url);

        return requestbody;
    }

    public static boolean isIsoDate(String str) {

        boolean res;
        try {
            Instant myDate = Instant.parse(str);
            res = true;
        } catch (DateTimeParseException e) {
            res = false;
        }
        return res;
    }
}