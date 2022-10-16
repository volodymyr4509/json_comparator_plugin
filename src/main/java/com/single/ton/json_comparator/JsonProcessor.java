package com.single.ton.json_comparator;

import java.util.*;

import static com.single.ton.json_comparator.JsonComparator.JSON_COMPARATOR;

public class JsonProcessor {


    public void deepSort(Object input) {
        if (input instanceof Map) {
            Map<String, Object> inputMap = (Map<String, Object>) input;
            for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
                deepSort(entry.getValue());
            }
            //TODO just sort LinkedHashMap by key in natural order to improve performance
            Map<String, Object> treeMap = new TreeMap<String, Object>(JSON_COMPARATOR);
            treeMap.putAll(inputMap);
            inputMap.clear();
            inputMap.putAll(treeMap);
        }
        if (input instanceof List) {
            for (Object it : ((List) input)) {
                deepSort(it);
            }
            ((List) input).sort(JSON_COMPARATOR);
        }
    }
}
