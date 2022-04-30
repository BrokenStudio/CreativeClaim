package dev.brokenstudio.creative.claim.manager;

public interface DualManager<T, K> {

    T get(String key, K k);
    void set(String key, K k, T t);
}
