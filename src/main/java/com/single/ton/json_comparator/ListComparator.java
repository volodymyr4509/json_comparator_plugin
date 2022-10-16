package com.single.ton.json_comparator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.single.ton.json_comparator.JsonComparator.JSON_COMPARATOR;

public class ListComparator implements Comparator<List<?>> {

    protected static final ListComparator LIST_COMPARATOR = new ListComparator();

    @Override
    public int compare(List<?> o1, List<?> o2) {
        Iterator<?> firstIterator = o1.iterator();
        Iterator<?> secondIterator = o2.iterator();
        int result = 0;
        while (firstIterator.hasNext() && result == 0) {
            if (secondIterator.hasNext()) {
                result = JSON_COMPARATOR.compare(firstIterator.next(), secondIterator.next());
            } else {
                result = -1;
            }
        }
        return result;
    }
}
