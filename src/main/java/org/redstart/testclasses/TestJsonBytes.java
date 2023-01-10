package org.redstart.testclasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class TestJsonBytes {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        TestJson testJson = new TestJson();

        System.out.println(testJson);

        try {


            String json = objectMapper.writeValueAsString(testJson);
            System.out.println(json);
            System.out.println();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
