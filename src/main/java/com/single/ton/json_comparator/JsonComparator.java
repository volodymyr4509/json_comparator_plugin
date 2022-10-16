package com.single.ton.json_comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;

public class JsonComparator implements Comparator {
    private static final Logger log = LoggerFactory.getLogger(JsonComparator.class);

    protected static final JsonComparator JSON_COMPARATOR = new JsonComparator();

    private static final Map<Class, Integer> SORT = Map.of(
            Boolean.class, 1,
            Integer.class, 2,
            BigInteger.class, 2,
            Double.class, 3,
            String.class, 4,
            Map.class, 5,
            TreeMap.class, 5,
            LinkedHashMap.class, 5,
            ArrayList.class, 6,
            List.class, 6
    );

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o2 == null) {
            return -1;
        }
        if (o1 == null) {
            return 1;
        }
        if (!Objects.equals(o1.getClass(), o2.getClass())) {
            return SORT.getOrDefault(o1.getClass(), 100) - SORT.getOrDefault(o2.getClass(), 100);
        }
        if (o1 instanceof Comparable) {
            return ((Comparable) o1).compareTo(o2);
        }
        if (o1 instanceof Map) {
            return MapComparator.MAP_COMPARATOR.compare((Map<String, ?>) o1, (Map<String, ?>) o2);
        }
        if (o1 instanceof List) {
           return ListComparator.LIST_COMPARATOR.compare((List<?>) o1, (List<?>) o2);
        }
        log.error("Unhandled node type {}, {}", o1.getClass(), o2.getClass());
        return 0;
    }
}
