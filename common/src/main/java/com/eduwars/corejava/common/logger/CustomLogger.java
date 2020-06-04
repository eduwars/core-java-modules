package com.eduwars.corejava.common.logger;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ravi Singh
 * @date 03/06/20-06-2020
 * @project eduwars
 */
@Slf4j
public final class CustomLogger {

    private CustomLogger() {

    }

    private static final Logger prettyLogger = LoggerFactory.getLogger("prettyLogger");
    private static final Logger colorLogger = LoggerFactory.getLogger("colorLogger");
    private  static final Logger fileLogger = LoggerFactory.getLogger("fileLogger");


    public static Logger getColorLogger() {
        return colorLogger;
    }

    public static Logger getPrettyLogger() {
        return prettyLogger;
    }

    public static Logger getFileLogger() {
        return  fileLogger;
    }



    public static void info(LoggerContext context, String message) {
        log.info(Markers.appendEntries(context.build()), message);
    }

    public static void error(LoggerContext context, String message) {
        CustomLogger.getFileLogger().error(Markers.appendEntries(context.build()), message);
        CustomLogger.getPrettyLogger().error(Markers.appendEntries(context.build()), message);
    }

    public static void error(LoggerContext context, String message, Throwable ex) {
        CustomLogger.getFileLogger().error(Markers.appendEntries(context.build()), message, ex);
        CustomLogger.getPrettyLogger().error(Markers.appendEntries(context.build()), message, ex);
    }

    public static void error(String message, Throwable ex) {
        CustomLogger.getFileLogger().error(message, ex);
        CustomLogger.getPrettyLogger().error( message, ex);
    }

    public static void debug(LoggerContext context, String message) {
        log.debug(Markers.appendEntries(context.build()), message);
    }

    public static void incomingApiCall(LoggerContext context, String message) {
        String timeTaken = context.build().get(LoggerConstants.KEYS.TIME_TAKEN.value());
        String traceid = context.build().get(LoggerConstants.KEYS.TRACEID.value());
        String  requestMethod = context.build().get(LoggerConstants.KEYS.HTTPREQUESMETHOD.value());
        String uri = context.build().get(LoggerConstants.KEYS.REQUESTURI.value());
        String status = context.build().get(LoggerConstants.KEYS.HTTPSTATUSCODE.value());
        String responseSize = context.build().get(LoggerConstants.KEYS.RESPONSE_SIZE.value());


        CustomLogger.getFileLogger().info(Markers.appendEntries(context.build()), message);
        CustomLogger.getColorLogger().info(String.format("%s <---%s<--- %s %s %s %s %s",traceid, "001", requestMethod, uri, status, timeTaken, responseSize));
    }

    public static void outgoingApiCall(LoggerContext context, String message) {

        String traceId = context.build().get(LoggerConstants.KEYS.TRACEID.value());
        String requestMethod = context.build().get(LoggerConstants.KEYS.HTTPREQUESMETHOD.value());
        String requestUri = context.build().get(LoggerConstants.KEYS.REQUESTURI.value());

        CustomLogger.getFileLogger().info(Markers.appendEntries(context.build()), message);
        CustomLogger.getColorLogger().info(String.format("%s --->%s---> %s %s",traceId, "001", requestMethod , requestUri));
    }

    /* should be used in once per request filter... means in after or before request method */
    public static void requestReceived(LoggerContext context) {

        String traceId = context.build().get(LoggerConstants.KEYS.TRACEID.value());
        String method = context.build().get(LoggerConstants.KEYS.HTTPREQUESMETHOD.value());
        String path = context.build().get(LoggerConstants.KEYS.REQUEST_PATH.value());

        CustomLogger.getFileLogger().info(Markers.appendEntries(context.build()), "Request Received");
        CustomLogger.getColorLogger().info(String.format("<---- Request %s ---- %s (%s)",traceId, path, method ));
    }

    /* should be used in once per request filter... means in after or before request method */
    public static void requestFinished(LoggerContext context) {

        String traceId = context.build().get(LoggerConstants.KEYS.TRACEID.value());
        String method = context.build().get(LoggerConstants.KEYS.HTTPREQUESMETHOD.value());
        String path = context.build().get(LoggerConstants.KEYS.REQUEST_PATH.value());
        String status = context.build().get(LoggerConstants.KEYS.HTTPSTATUSCODE.value());

        CustomLogger.getFileLogger().info(Markers.appendEntries(context.build()), "Request Finished");
        CustomLogger.getColorLogger().info(String.format("---- Response %s ----> %s (%s) %s", traceId, path, method, status));
    }

}
