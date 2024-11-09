package dev.da0hn.structured.concurrency.api.bestprice.bookstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestCallStatistics {

    private final Map<String, Long> timeMap = Collections.synchronizedMap(new HashMap<>());

    public Map<String, Long> getTimeMap() {
        return this.timeMap;
    }

    public void addTiming(String key, long time) {
        this.timeMap.put(key, time);
    }


}
