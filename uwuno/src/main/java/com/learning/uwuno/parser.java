package com.learning.uwuno;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.uwuno.errors.*;

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

    public String getValue(String key) {
        if (map.get(key) == null) {
            throw new errorNotFound();
        } else {
            return map.get(key);
        }
    }
}