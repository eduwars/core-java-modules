package com.eduwars.corejava.common.models.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Ravi Singh
 * @date 04/06/20-06-2020
 * @project eduwars
 */
@Getter
@Setter
@SuperBuilder
public class Request {

    private UriComponentsBuilder uriBuilder;

    private HttpHeaders headers;

    private Object body;

    private String url;

}