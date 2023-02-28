package scorex.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringTokenizerTest {
    @Test
    void hasMoreTokens() {
        assertFalse(new StringTokenizer("", ",").hasMoreTokens());
        assertFalse(new StringTokenizer(",", ",").hasMoreTokens());
        assertTrue(new StringTokenizer("a,", ",").hasMoreTokens());
        assertTrue(new StringTokenizer("a,b", ",").hasMoreTokens());
        assertTrue(new StringTokenizer(",b", ",").hasMoreTokens());
    }

    @Test
    void nextToken() {
        String input = "a,b,c,d,e,";
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            builder.append(tokenizer.nextToken()).append(",");
        }
        assertEquals(input, builder.toString());
    }

    @Test
    void countTokens() {
        String input = "a,b,c,d,e";
        StringTokenizer tokenizer = new StringTokenizer(input, ",");
        int count = tokenizer.countTokens();
        assertEquals(5, count);
    }
}