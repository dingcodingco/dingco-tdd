package com.dingcolabs.dingcotdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringMethodsTest {

    @Test
    void testUpper() {
        assertEquals("FOO", "foo".toUpperCase());
    }

    @Test
    void testIsUpper() {
        assertTrue("FOO".equals("FOO".toUpperCase()));
        assertFalse("Foo".equals("Foo".toUpperCase()));
    }

    @Test
    void testSplit() {
        String s = "hello world";
        assertArrayEquals(new String[]{"hello", "world"}, s.split(" "));
        // check that parsing fails when the input is not a number
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("two"));
    }
}
