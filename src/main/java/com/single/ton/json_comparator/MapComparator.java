package com.single.ton.json_comparator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import static com.single.ton.json_comparator.JsonComparator.JSON_COMPARATOR;

public class MapComparator implements Comparator<Map<String, ?>> {
    protected static final MapComparator MAP_COMPARATOR = new MapComparator();

    @Override
    public int compare(Map<String, ?> o1, Map<String, ?> o2) {
        Iterator<? extends Map.Entry<String, ?>> firstIterator = o1.entrySet().iterator();
        Iterator<? extends Map.Entry<String, ?>> secondIterator = o2.entrySet().iterator();
        int result = 0;
        while (firstIterator.hasNext() && result == 0) {
            Map.Entry<String, ?> firstEntry = firstIterator.next();
            if (secondIterator.hasNext()) {
                Map.Entry<String, ?> secondEntry = secondIterator.next();
                result = firstEntry.getKey().compareTo(secondEntry.getKey());
                if (result == 0) {
                    result = JSON_COMPARATOR.compare(firstEntry.getValue(), secondEntry.getValue());
                }
            } else {
                result = -1;
            }
        }
        return result;
    }
}
