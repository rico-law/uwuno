package com.learning.uwuno.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.uwuno.errors.internalServerError;

import java.util.Map;

public final class requestUtil {
    // Should not be instantiated
    private requestUtil() {
        throw new UnsupportedOperationException();
    }

    public static Map<String, String> parseJson(String json) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            return map;
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }
}