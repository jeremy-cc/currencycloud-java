package com.currencycloud.client.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.InvocationAware;
import si.mazi.rescu.RestInvocation;

import javax.annotation.Nullable;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonPropertyOrder({"platform", "request", "response", "error_code", "errors"})
public abstract class CurrencyCloudException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(ApiException.class);
    private static final YAMLFactory YAML_FACTORY = new YAMLFactory();
    private static final String platform = String.format(
            "Java %s (%s)",
            System.getProperty("java.version"),
            System.getProperty("java.vendor")
    );
    private static final JacksonAnnotationIntrospector IGNORE_EXCEPTION_PROPERTIES = new JacksonAnnotationIntrospector() {
        @Override
        public boolean hasIgnoreMarker(final AnnotatedMember m) {
            Class<?> declaringClass = m.getDeclaringClass();
            return declaringClass.isAssignableFrom(RuntimeException.class)
                    || super.hasIgnoreMarker(m);
        }
    };

    private Request request;

    protected CurrencyCloudException(String message, Throwable cause) {
        super(message, cause);
        if (cause instanceof InvocationAware) {
            setInvocation(((InvocationAware)cause).getInvocation());
        }
    }

    protected void setInvocation(@Nullable RestInvocation invocation) {
        if (invocation != null) {
            Map<String, String> params = new LinkedHashMap<>();
            for (Class<? extends Annotation> paramAnn : Arrays.asList(FormParam.class, QueryParam.class)) {
                params.putAll(invocation.getParamsMap().get(paramAnn).asHttpHeaders());
            }
            this.request = new Request(params, invocation.getHttpMethod(), invocation.getInvocationUrl());
        }
    }

    public Request getRequest() {
        return request;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper(YAML_FACTORY);
            mapper.setAnnotationIntrospector(IGNORE_EXCEPTION_PROPERTIES);
            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, this);
            return writer.toString();
        } catch (Exception e) {
            log.warn("Error formatting exception as YAML: " + e);
            return super.toString();
        }
    }

    @JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
    public static class Request {
        public final Map<String, String> parameters;
        public final String verb;
        public final String url;

        public Request(Map<String, String> parameters, String httpMethod, String url) {
            this.parameters = parameters;
            this.verb = httpMethod.toLowerCase();
            this.url = url;
        }
    }
}
