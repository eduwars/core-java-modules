package com.eduwars.corejava.common.connector;

import com.eduwars.corejava.common.logger.LoggerConstants;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;

/**
 * @author Ravi Singh
 * @date 03/06/20-06-2020
 * @project eduwars
 */

@Builder
@Getter
@ToString
public class ConnectorRequestBody {

    private URI uri;

    private HttpMethod method;

    private HttpHeaders headers;

    private Object body;

    private LoggerConstants.STATE state;
}
