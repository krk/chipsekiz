package dev.krk.chipsekiz.hal;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FramebufferTest {
    @Test void draw() throws IOException {
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

        assertFalse(fb.draw((byte) 0, (byte) 0, true));
        assertFalse(fb.draw((byte) 1, (byte) 1, true));
        assertFalse(fb.draw((byte) 1, (byte) 2, true));
        assertFalse(fb.draw((byte) 4, (byte) 4, true));
        assertFalse(fb.draw((byte) 7, (byte) 3, true));
        assertEquals("""
            ┌────────┐
            │█       │
            │ █      │
            │ █      │
            │       █│
            │    █   │
            └────────┘
            """, fb.toString());

        assertTrue(fb.draw((byte) 1, (byte) 1, true));
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
        assertFalse(fb.draw((byte) 10, (byte) 9, true));
        assertEquals("""
            ┌────────┐
            │█       │
            │        │
            │ █      │
            │       █│
            │  █ █   │
            └────────┘
            """, fb.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fb.writeImage(1, 1, outputStream);
        assertEquals(
            "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAFAQAAAABQ6pD4AAAAEklEQVR4XmOoZ/jPsJ/hH8N1ABMGBBNtSDdyAAAAAElFTkSuQmCC",
            Base64.getEncoder().encodeToString(outputStream.toByteArray()));

        assertFalse(fb.getPixel((byte) 1, (byte) 1));

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
