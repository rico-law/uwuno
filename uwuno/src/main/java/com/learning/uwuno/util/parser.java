package com.learning.uwuno.util;

import com.learning.uwuno.errors.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class parser {
    // Class variables
    private Map<String, String> map;

    // Class functions
    public parser(String json) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }

    // Assume this is called after checking for existence if handler handles multiple formats
    public String getValue(String key) {
        if (!exists(key)) {
            throw new errorNotFound();
        }
        return map.get(key);
    }

    public boolean exists(String key) {
        return map.get(key) != null;
    }
}
