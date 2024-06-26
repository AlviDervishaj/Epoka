package maps;

import java.util.ArrayList;

public abstract class AbstractMap<K, V> implements Map<K, V> {
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    // --- nested class to represent key/value pairs in Map
    protected static class MapItem<K, V> implements Item<K, V> {
        private K key;
        private V value;

        public MapItem(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        @Override
        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    } // --- end of nested MapItem class

    @Override
    public Iterable<K> keys() {
        ArrayList<K> keys = new ArrayList<>();
        for(Item<K, V> item : items()) // reusing the items() abstract method
            keys.add(item.getKey());
        return keys;
    }

    @Override
    public Iterable<V> values() {
        ArrayList<V> values = new ArrayList<>();
        for(Item<K, V> item : items()) // reusing the items() abstract method
            values.add(item.getValue());
        return values;
    }
}
