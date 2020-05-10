package dev.krk.emulator.chipsekiz.hal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FramebufferTest {
    @Test void draw() {
        Framebuffer fb = new Framebuffer(8, 5);

        assertEquals(8, fb.getWidth());
        assertEquals(5, fb.getHeight());
        assertEquals("""
            ┌────────┐
            │        │
            │        │
            │        │
            │        │
            │        │
            └────────┘
            """, fb.toString());

        assertEquals(false, fb.draw((byte) 0, (byte) 0, true));
        assertEquals(false, fb.draw((byte) 1, (byte) 1, true));
        assertEquals(false, fb.draw((byte) 1, (byte) 2, true));
        assertEquals(false, fb.draw((byte) 4, (byte) 4, true));
        assertEquals(false, fb.draw((byte) 7, (byte) 3, true));
        assertEquals("""
            ┌────────┐
            │█       │
            │ █      │
            │ █      │
            │       █│
            │    █   │
            └────────┘
            """, fb.toString());

        assertEquals(true, fb.draw((byte) 1, (byte) 1, true));
        assertEquals("""
            ┌────────┐
            │█       │
            │        │
            │ █      │
            │       █│
            │    █   │
            └────────┘
            """, fb.toString());

        // wrap
        assertEquals(false, fb.draw((byte) 10, (byte) 9, true));
        assertEquals("""
            ┌────────┐
            │█       │
            │        │
            │ █      │
            │       █│
            │  █ █   │
            └────────┘
            """, fb.toString());

        assertEquals(false, fb.getPixel((byte) 1, (byte) 1));

        fb.clearScreen();
        assertEquals("""
            ┌────────┐
            │        │
            │        │
            │        │
            │        │
            │        │
            └────────┘
            """, fb.toString());
    }
}
