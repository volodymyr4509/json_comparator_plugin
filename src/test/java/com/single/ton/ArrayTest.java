package com.single.ton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.single.ton.json_comparator.JsonProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ArrayTest {

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void diffTypesTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("array/diff_types.json");
        processor.deepSort(inputJson);
        Object expected = readFile("array/diff_types_sorted.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    @Test
    public void objectsSameFieldsTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("array/objects_same_fields.json");
        processor.deepSort(inputJson);
        Object expected = readFile("array/objects_same_fields_sorted.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    @Test
    public void arrayOfArraysTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("array/array_of_arrays.json");
        processor.deepSort(inputJson);
        Object expected = readFile("array/array_of_arrays_sorted.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    @Test
    public void objectsDiffFieldsTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("array/objects_diff_fields.json");
        processor.deepSort(inputJson);
        Object expected = readFile("array/objects_diff_fields_sorted.json");

        Assert.assertEquals(toPrettyString(expected), toPrettyString(inputJson));
    }

    @Test
    public void emptyArrayTest() {
        JsonProcessor processor = new JsonProcessor();
        Object inputJson = readFile("array/empty_array.json");
        processor.deepSort(inputJson);
        Object expected = readFile("array/empty_array_sorted.json");

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
