package ru.finex.core.cluster.impl.adapter;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import ru.finex.core.cluster.Map;

import java.util.Collection;
import java.util.Set;

/**
 * @param <K> key
 * @param <V> value
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class MapAdapter<K, V> implements Map<K, V> {

    private final RMap<K, V> map;

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        return map.replace(key, value);
    }

    @Override
    public void putAll(java.util.Map<? extends K, ? extends V> map) {
        this.map.putAll(map);
    }

    @SafeVarargs
    @Override
    public final long fastRemove(K... keys) {
        return map.fastRemove(keys);
    }

    @Override
    public boolean fastPut(K key, V value) {
        return map.fastPut(key, value);
    }

    @Override
    public boolean fastReplace(K key, V value) {
        return map.fastReplace(key, value);
    }

    @Override
    public boolean fastPutIfAbsent(K key, V value) {
        return map.fastPutIfAbsent(key, value);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return map.equals(obj);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
