package scorex.util;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HashMapTest {

    static Object[] keys = {
            null, "",
            "key1", "key2", "key3",
            0, 20f, 30d, Boolean.TRUE,
            "key4", "key5", "key6"};
    static Object[] values = {
            null, "",
            "val1", "val2", "val3",
            0, 20f, 30d, Boolean.TRUE,
            "val4", "val5", "val6"};

    @Test
    void invalidCapacity() {
        HashMap<String, String> map = null;
        int[] invalidCapacity = {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -10, -2, -1};
        for (int invalid : invalidCapacity) {
            try {
                map = new HashMap<>(invalid);
                fail("Unexpected success: " + invalid);
            } catch (IllegalArgumentException e) {
                // Success
            }
        }
    }

    @Test
    void isEmpty() {
        HashMap<String, String> map = new HashMap<>();
        assertTrue(map.isEmpty());
        map.put("k", "v");
        assertFalse(map.isEmpty());
    }

    @Test
    void putAndGet() {
        HashMap<Object, Object> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            assertFalse(map.containsKey(keys[i]));
            assertFalse(map.containsValue(values[i]));

            map.put(keys[i], values[i]);
            assertEquals(map.size(), i + 1);
            assertTrue(map.containsKey(keys[i]));
            assertTrue(map.containsValue(values[i]));
            for (int j = 0; j < (i + 1); j++) {
                assertEquals(map.get(keys[j]), values[j]);
            }
        }
    }

    @Test
    void remove() {
        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys.length; j++) {
                HashMap<Object, Object> map = new HashMap<>();
                for (int k = 0; k <= i; k++) {
                    map.put(keys[k], values[k]);
                }
                Object result = map.remove(keys[j]);
                for (int k = 0; k <= i; k++) {
                    if (k != j) {
                        assertEquals(map.get(keys[k]), values[k]);
                    } else {
                        assertTrue(map.get(keys[k]) == null || result == values[k]);
                    }
                }
            }
        }
    }

    @Test
    void keySet() {
        for (int i = 0; i < keys.length; i++) {
            HashMap<Object, Object> map = new HashMap<>();
            for (int j = 0; j <= i; j++) {
                map.put(keys[j], values[j]);
            }
            Set<Object> set = map.keySet();
            assertEquals(set.size(), map.size());
            Iterator<Object> iter = set.iterator();
            while (iter.hasNext()) {
                Object entry = iter.next();
                assertTrue(map.containsKey(entry));
            }
        }
    }

    @Test
    void values() {
        for (int i = 0; i < keys.length; i++) {
            HashMap<Object, Object> map = new HashMap<>();
            for (int j = 0; j <= i; j++) {
                map.put(keys[j], values[j]);
            }
            Collection<Object> set = map.values();
            assertEquals(set.size(), map.size());
            Iterator<Object> iter = set.iterator();
            while (iter.hasNext()) {
                Object value = iter.next();
                assertTrue(map.containsValue(value));
            }
        }
    }

    @Test
    void entrySet() {
        for (int i = 0; i < keys.length; i++) {
            HashMap<Object, Object> map = new HashMap<>();
            for (int j = 0; j <= i; j++) {
                map.put(keys[j], values[j]);
            }
            Set<Map.Entry<Object, Object>> set = map.entrySet();
            assertEquals(set.size(), map.size());
            Iterator<Map.Entry<Object, Object>> iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry<Object, Object> entry = iter.next();
                assertNotNull(entry);
                assertEquals(map.get(entry.getKey()), entry.getValue());
            }
        }
    }

    @Test
    void putAll() {
        HashMap<Object, Object> map1 = new HashMap<>();
        HashMap<Object, Object> map2 = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map1.put(keys[i], values[i]);
        }

        map1.putAll(map2);
        assertEquals(map1.size(), keys.length);
        for (int i = 0; i < keys.length; i++) {
            assertEquals(map1.get(keys[i]), values[i]);
        }

        map2.putAll(map1);
        assertEquals(map2.size(), keys.length);
        for (int i = 0; i < keys.length; i++) {
            assertEquals(map2.get(keys[i]), values[i]);
        }
    }
}
