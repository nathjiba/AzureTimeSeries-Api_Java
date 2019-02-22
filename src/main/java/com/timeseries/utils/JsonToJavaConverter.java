package com.timeseries.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json to Java model converter
 *
 */
public class JsonToJavaConverter {

    private final static Logger logger = LoggerFactory.getLogger(JsonToJavaConverter.class);

    public static Object convertJsonToJava(String json, Object objectToBeConverted)
            throws JsonParseException, JsonMappingException, IOException {
        // converting JSON String to Java object
        return fromJson(json, objectToBeConverted);
    }

    public static Object fromJson(String json, Object objectToBeConverted)
            throws JsonParseException, JsonMappingException, IOException {
        Object convertedObject = new ObjectMapper().readValue(json, objectToBeConverted.getClass());
        logger.info("JSON String : " + json);
        logger.info("Java Object : " + convertedObject);
        return convertedObject;
    }
}