package scorex.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ArrayListTest {
    @Test
    void positiveCapacity() {
        for (int capacity : new int[] {0, 1, 10}) {
            try {
                new ArrayList<Object>(capacity);
            } catch (IllegalArgumentException e) {
                fail("not expected");
            }
        }
    }

    @Test
    void negativeCapacity() {
        for (int capacity : new int[] {-1, Integer.MIN_VALUE}) {
            try {
                new ArrayList<Object>(capacity);
                fail("IllegalArgumentException expected");
            } catch (IllegalArgumentException e) {
                // Success
            }
        }
    }

    private ArrayList<Integer> createList(int size) {
        ArrayList<Integer> al = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            al.add(i);
        }
        return al;
    }

    @Test
    void addAndRemove() {
        ArrayList<Integer> al = createList(10);
        al.add(100);
        al.add(101);
        assertEquals(12, al.size());
        al.remove(0);
        al.remove(0);
        assertEquals(10, al.size());
        assertEquals(2, al.get(0));
        al.add(0, 200);
        al.add(1, 201);
        assertEquals(12, al.size());
    }

    @Test
    void indexOf() {
        ArrayList<Integer> al = createList(5);
        assertEquals(0, al.indexOf(0));
        assertEquals(-1, al.indexOf(5));
        assertEquals(-1, al.indexOf(null));
    }

    @Test
    void get() {
        ArrayList<Integer> al = createList(5);
        assertEquals(5, al.size());
        assertEquals(al.size()-1, al.get(al.size()-1));
    }

    @Test
    void iterator() {
        ArrayList<Integer> al = createList(0);
        assertFalse(al.iterator().hasNext());

        ArrayList<Integer> al2 = createList(5);
        var iter2 = al2.iterator();
        for (int i = 0; i < al2.size(); i++) {
            assertEquals(al2.get(i), iter2.next());
        }

        ArrayList<Integer> al3 = createList(5);
        var iter3 = al3.iterator();
        al3.add(100);
        try {
            iter3.next();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // Success: ConcurrentModification
        }
    }

    @Test
    void listIterator() {
        ArrayList<Integer> al = createList(0);
        assertFalse(al.listIterator().hasNext());

        ArrayList<Integer> al2 = createList(5);
        var iter2 = al2.listIterator();
        for (int i = 0; i < al2.size(); i++) {
            assertEquals(al2.get(i), iter2.next());
        }

        ArrayList<Integer> al3 = createList(5);
        var iter3 = al3.listIterator();
        al3.add(100);
        try {
            iter3.next();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // Success: ConcurrentModification
        }
    }

    @Test
    void listIteratorInt() {
        ArrayList<Integer> al = createList(0);
        assertFalse(al.listIterator(0).hasNext());

        ArrayList<Integer> al2 = createList(5);
        int[] arr = {0, al2.size()-1};
        for (int index : arr) {
            var iter2 = al2.listIterator(index);
            for (int i = index; (i < al2.size()) && (iter2.hasNext()); i++) {
                assertEquals(al2.get(i), iter2.next());
            }
        }

        ArrayList<Integer> al3 = createList(5);
        var iter3 = al3.listIterator(al3.size()-1);
        al3.add(100);
        try {
            iter3.next();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            // Success: ConcurrentModification
        }
    }

    @Test
    void subList() {
        List<String> recents = new ArrayList<>() {{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
        }};
        assertEquals(5, recents.size());
        assertEquals("[1, 2, 3, 4, 5]", recents.toString());
        assertTrue(recents.contains("2"));

        recents = recents.subList(1, 5);
        assertEquals(4, recents.size());
        assertEquals("[2, 3, 4, 5]", recents.toString());
        assertTrue(recents.contains("2"));

        recents.add("6");
        assertEquals(5, recents.size());
        assertEquals("[2, 3, 4, 5, 6]", recents.toString());
        assertTrue(recents.contains("2"));

        recents = recents.subList(1, 4);
        assertEquals(3, recents.size());
        assertEquals("[3, 4, 5]", recents.toString());
        assertFalse(recents.contains("2"));
    }

    @Test
    void testEquals() {
        ArrayList<Integer> al = createList(4);
        ArrayList<Integer> al2 = createList(4);
        assertEquals(al, al2);
        assertFalse(al.equals(null));
    }

    @Test
    void testToString() {
        ArrayList<String> al = new ArrayList<>();
        al.add("string1");
        al.add("string2");
        String expected = "[string1, string2]";
        assertEquals(expected, al.toString());
    }
}
