package scorex.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64Test {
    static String input = "Hello world!";
    static String urlInput = "http://example.com";

    @Test
    void encode() {
        byte[] encoded = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        byte[] expected = java.util.Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expected, encoded);
    }

    @Test
    void urlEncode() {
        byte[] encoded = Base64.getUrlEncoder().encode(urlInput.getBytes(StandardCharsets.UTF_8));
        byte[] expected = java.util.Base64.getUrlEncoder().encode(urlInput.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expected, encoded);
    }

    @Test
    void encodeAndDecode() {
        byte[] encoded = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.getDecoder().decode(encoded);
        assertEquals(input, new String(decoded));
    }

    @Test
    void urlEncodeAndDecode() {
        byte[] encoded = Base64.getUrlEncoder().encode(urlInput.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.getUrlDecoder().decode(encoded);
        assertEquals(urlInput, new String(decoded));
    }
}
