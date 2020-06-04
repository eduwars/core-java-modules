package com.eduwars.corejava.common.models.streams;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Ravi Singh
 * @date 04/06/20-06-2020
 * @project eduwars
 */
@Getter
@Setter
@SuperBuilder
public class Streams {

    private String traceId;

    private String accessToken;

    private String clientId;


}