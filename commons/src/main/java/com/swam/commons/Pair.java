package com.swam.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Pair<K, V> {
    private K key;
    private V value;

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<K, V>(key, value);
    }
}
