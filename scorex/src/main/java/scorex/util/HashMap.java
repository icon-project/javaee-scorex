package scorex.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/*
 * A hash table based implementation of the Map interface.
 */
public class HashMap<K, V> implements Map<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1 << 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final float loadFactor;
    private int threshold;
    private Entry<K,V>[] table;
    private int size;
    private int modCount;

    private Set<K> keySet;
    private Collection<V> values;
    private Set<Map.Entry<K,V>> entrySet;

    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity)
            capacity <<= 1;

        this.loadFactor = loadFactor;
        threshold = (int)(capacity * loadFactor);
        table = (Entry<K,V>[]) new Entry[capacity];
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Entry<K,V>[]) new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int indexFor(int h, int length) {
        return h & (length-1);
    }

    @Override
    public V get(Object key) {
        Entry<K,V> e = getEntry(key);
        if (e != null)
            return e.value;
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    private Entry<K, V> getEntry(Object key) {
        int hash = (key == null) ? 0 : hash(key.hashCode());
        for (Entry<K,V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = (key == null) ? 0 : hash(key.hashCode());
        int i = indexFor(hash, table.length);
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    private void addEntry(int hash, K key, V value, int index) {
        Entry<K,V> e = table[index];
        table[index] = new Entry<K,V>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
    }

    private void resize(int newCapacity) {
        Entry<K,V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        @SuppressWarnings("unchecked")
        Entry<K,V>[] newTable = (Entry<K,V>[]) new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }

    private void transfer(Entry<K, V>[] newTable) {
        Entry<K,V>[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K,V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K,V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    @Override
    public V remove(Object key) {
        Entry<K,V> e = removeEntryForKey(key);
        return (e == null ? null : e.value);
    }

    private Entry<K, V> removeEntryForKey(Object key) {
        int hash = (key == null) ? 0 : hash(key.hashCode());
        int i = indexFor(hash, table.length);
        Entry<K,V> prev = table[i];
        Entry<K,V> e = prev;
        while (e != null) {
            Entry<K,V> next = e.next;
            Object k;
            if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                modCount++;
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                return e;
            }
            prev = e;
            e = next;
        }
        return e;
    }

    @Override
    public String toString() {
        Iterator<Map.Entry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Map.Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    static class Entry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;
        final int hash;

        Entry(int h, K k, V v, Entry<K,V> n) {
            hash = h;
            key = k;
            value = v;
            next = n;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            @SuppressWarnings("unchecked")
            Map.Entry<K,V> e = (Map.Entry<K,V>)o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public final int hashCode() {
            return (key==null ? 0 : key.hashCode()) ^
                    (value==null ? 0 : value.hashCode());
        }

        @Override
        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<?, ?> e : m.entrySet()) {
            this.put((K) e.getKey(), (V) e.getValue());
        }
    }

    @Override
    public void clear() {
        modCount++;
        Entry<K,V>[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null)
            return containsNullValue();

        Entry<K,V>[] tab = table;
        for (int i = 0; i < tab.length; i++)
            for (Entry<K,V> e = tab[i]; e != null ; e = e.next)
                if (value.equals(e.value))
                    return true;
        return false;
    }

    private boolean containsNullValue() {
        Entry<K,V>[] tab = table;
        for (int i = 0; i < tab.length; i++)
            for (Entry<K,V> e = tab[i]; e != null ; e = e.next)
                if (e.value == null)
                    return true;
        return false;
    }

    @Override
    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = values;
        return (vs != null ? vs : (values = new Values()));
    }

    private abstract static class AbstractCollection<E> implements Collection<E> {
        protected AbstractCollection() {
        }

        public abstract Iterator<E> iterator();

        public abstract int size();

        public boolean isEmpty() {
            return size() == 0;
        }

        public boolean contains(Object o) {
            Iterator<E> e = iterator();
            if (o==null) {
                while (e.hasNext())
                    if (e.next()==null)
                        return true;
            } else {
                while (e.hasNext())
                    if (o.equals(e.next()))
                        return true;
            }
            return false;
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> c) {
            boolean modified = false;
            Iterator<? extends E> e = c.iterator();
            while (e.hasNext()) {
                if (add(e.next()))
                    modified = true;
            }
            return modified;
        }

        public boolean containsAll(Collection<?> c) {
            Iterator<?> e = c.iterator();
            while (e.hasNext())
                if (!contains(e.next()))
                    return false;
            return true;
        }

        public boolean removeAll(Collection<?> c) {
            boolean modified = false;
            Iterator<?> e = iterator();
            while (e.hasNext()) {
                if (c.contains(e.next())) {
                    e.remove();
                    modified = true;
                }
            }
            return modified;
        }

        public boolean retainAll(Collection<?> c) {
            boolean modified = false;
            Iterator<?> e = iterator();
            while (e.hasNext()) {
                if (!c.contains(e.next())) {
                    e.remove();
                    modified = true;
                }
            }
            return modified;
        }

        public boolean remove(Object o) {
            Iterator<E> e = iterator();
            if (o==null) {
                while (e.hasNext()) {
                    if (e.next()==null) {
                        e.remove();
                        return true;
                    }
                }
            } else {
                while (e.hasNext()) {
                    if (o.equals(e.next())) {
                        e.remove();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private final class KeySet extends AbstractCollection<K> implements Set<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            return containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return HashMap.this.removeEntryForKey(o) != null;
        }

        @Override
        public void clear() {
            HashMap.this.clear();
        }
    }

    private final class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public void clear() {
            HashMap.this.clear();
        }
    }

    private final class KeyIterator extends HashIterator<K> {
        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class ValueIterator extends HashIterator<V> {
        public V next() {
            return nextEntry().getValue();
        }
    }

    private final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
        public Map.Entry<K,V> next() {
            return nextEntry();
        }
    }

    private abstract class HashIterator<E> implements Iterator<E> {
        Entry<K,V> next;
        int expectedModCount;
        int index;
        Entry<K,V> current;

        HashIterator() {
            expectedModCount = modCount;
            if (size > 0) {
                Entry<K,V>[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Entry<K,V> nextEntry() {
            if (modCount != expectedModCount)
                throw new RuntimeException();
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();

            if ((next = e.next) == null) {
                Entry<K,V>[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
            current = e;
            return e;
        }

        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new RuntimeException();
            Object k = current.key;
            current = null;
            HashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K,V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    private final class EntrySet extends AbstractCollection<Map.Entry<K, V>> implements Set<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            @SuppressWarnings("unchecked")
            Map.Entry<K,V> e = (Map.Entry<K,V>) o;
            Entry<K,V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        @Override
        public boolean remove(Object o) {
            return removeMapping(o) != null;
        }

        @Override
        public void clear() {
            HashMap.this.clear();
        }
    }

    final Entry<K, V> removeMapping(Object o) {
        if (!(o instanceof Map.Entry))
            return null;
        @SuppressWarnings("unchecked")
        Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
        Object key = entry.getKey();
        int hash = (key == null) ? 0 : hash(key.hashCode());
        int i = indexFor(hash, table.length);
        Entry<K,V> prev = table[i];
        Entry<K,V> e = prev;
        while (e != null) {
            Entry<K,V> next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                return e;
            }
            prev = e;
            e = next;
        }
        return e;
    }
}
