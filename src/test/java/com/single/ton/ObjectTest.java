package com.single.ton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.ton.json_comparator.JsonProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ObjectTest {

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void nullTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("object/null_object.json");
        processor.deepSort(inputJson);
        Object expected = readFile("object/null_object.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    @Test
    public void emptyObjectTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("object/empty_object.json");
        processor.deepSort(inputJson);
        Object expected = readFile("object/empty_object_sorted.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    @Test
    public void simpleObjectTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("object/simple_object.json");
        processor.deepSort(inputJson);
        Object expected = readFile("object/simple_object_sorted.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    private Object readFile(String fileName) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            return mapper.readValue(resourceAsStream, Object.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toPrettyString(Object json) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
