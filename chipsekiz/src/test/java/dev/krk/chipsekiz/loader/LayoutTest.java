package dev.krk.chipsekiz.loader;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayoutTest {
    private static void assertLayoutThrows(Layout layout) {
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(0, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(-1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(0, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(0, 1, -1));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(0, 2, 1));
    }

    @Test void emptySection() {
        Layout layout = new Layout(Collections.singletonList(new Section(0, new byte[] {})));

        assertLayoutThrows(layout);

        assertFalse(layout.isValid(0, 1, 1));
    }

    @Test void invalidLayouts() {
        Layout layout =
            new Layout(Collections.singletonList(new Section(0, new byte[] {(byte) 0xEE})));

        assertLayoutThrows(layout);

        assertFalse(layout.isValid(0, 1, 1));
        assertFalse(layout.isValid(0, 2, 10));
        assertTrue(layout.isValid(1, 2, 10));
        assertTrue(layout.isValid(8, 2, 10));

        assertThrows(IllegalArgumentException.class, () -> layout.isValid(9, 2, 10));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(10, 2, 10));
        assertThrows(IllegalArgumentException.class, () -> layout.isValid(11, 2, 10));
    }

    @Test void validLayouts() {
        Layout layout = new Layout(Arrays.asList(new Section(1, new byte[] {(byte) 0xFF, (byte) 0x0F}),
            new Section(0xF00, new byte[0xFF]), new Section(0xEA0, new byte[0x60])));

        assertTrue(layout.isValid(0x200, 1, 0x1000));
        assertTrue(layout.isValid(0x200, 1000, 0x1000));
        assertTrue(layout.isValid(0x200, 3000, 0x1000));
        assertTrue(layout.isValid(0x200, 3231, 0x1000));
        assertTrue(layout.isValid(0x200, 3232, 0x1000));
        assertFalse(layout.isValid(0x200, 3233, 0x1000));
    }

    @Test void overlappingSections() {
        Layout layout = new Layout(Arrays
            .asList(new Section(1, new byte[1]), new Section(0, new byte[2])));

        assertFalse(layout.isValid(5, 1, 100));
    }
}

