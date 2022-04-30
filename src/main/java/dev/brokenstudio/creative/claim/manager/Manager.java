package dev.brokenstudio.creative.claim.manager;

import java.util.HashSet;

public interface Manager<T> {

    T get(String key);
    void set(String key, T t);
    void load();

}
