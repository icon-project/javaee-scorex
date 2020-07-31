package scorex.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HashSetTest {
    @Test
    void validCapacity() {
        int[] validCapacity = {0, 1, 10, 100};
        for (int valid : validCapacity) {
            try {
                var set = new HashSet<>(valid);
                assertTrue(set.isEmpty());
            } catch (IllegalArgumentException e) {
                fail("Unexpected exception: " + valid);
            }
        }
    }

    @Test
    void invalidCapacity() {
        int[] invalidCapacity = {Integer.MIN_VALUE, -10, -1};
        for (int invalid : invalidCapacity) {
            try {
                var set = new HashSet<>(invalid);
                fail("Unexpected success: " + invalid);
            } catch (IllegalArgumentException e) {
                // Success
            }
        }
    }

    @Test
    void isEmpty() {
        HashSet<String> set = new HashSet<>();
        assertTrue(set.isEmpty());
        set.add("one");
        assertFalse(set.isEmpty());
    }

    @Test
    void addAndClear() {
        Object[] inputs = {null, "Hello World", 50.0, 100};
        HashSet<Object> set = new HashSet<>();
        for (Object value : inputs) {
            assertTrue(set.add(value));
            assertTrue(set.contains(value));
        }
        int s1 = set.size();
        assertEquals(s1, inputs.length);

        Object[] duplicates = {null, "Hello World", 50.0, 100};
        for (Object duplicate : duplicates) {
            assertFalse(set.add(duplicate));
            assertTrue(set.contains(duplicate));
        }
        assertEquals(s1, set.size());

        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    void iterator() {
        HashSet<Object> set = new HashSet<>();
        Iterator<Object> it = set.iterator();
        try {
            it.next();
            fail("NoSuchElementException expected");
        } catch (NoSuchElementException e) {
            // Success
        }

        Object[] inputs = {null, "Hello World", 50.0, 100};
        set.addAll(Arrays.asList(inputs));

        it = set.iterator();
        HashSet<Object> copy = new HashSet<>();
        while (it.hasNext()) {
            copy.add(it.next());
        }
        assertEquals(set, copy);
    }
}
