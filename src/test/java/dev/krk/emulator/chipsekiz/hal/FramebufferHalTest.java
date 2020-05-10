package dev.krk.emulator.chipsekiz.hal;

import dev.krk.emulator.chipsekiz.Decoder;
import dev.krk.emulator.chipsekiz.interpreter.Executor;
import dev.krk.emulator.chipsekiz.interpreter.IExecutor;
import dev.krk.emulator.chipsekiz.interpreter.Interpreter;
import dev.krk.emulator.chipsekiz.loader.Layout;
import dev.krk.emulator.chipsekiz.loader.Loader;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FramebufferHalTest {
    @Test void sound() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = new Executor();
        FramebufferHal hal = new FramebufferHal(1, 1, character -> (short) 0);

        for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
            byte[] program = new byte[] {0x65, (byte) imm, (byte) 0xF5, 0x18, 0x10, 0x04};

            Interpreter interpreter =
                new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0, program,
                    program.length, Layout.empty());

            interpreter.tick();
            assertFalse(hal.isSoundActive());

            byte timer = (byte) imm;
            while (timer != 0) {
                interpreter.tick();

                assertTrue(hal.isSoundActive());
                timer--;
            }

            interpreter.tick();
            if (imm != 0) {
                assertFalse(hal.isSoundActive());
            }

            hal.sound(false);
        }
    }

    @Test void framebufferDirty() {
        FramebufferHal hal = new FramebufferHal(1, 1, character -> (short) 0);
        assertFalse(hal.framebufferDirty());

        hal.draw((byte) 0, (byte) 0, false);
        assertTrue(hal.framebufferDirty());
        assertEquals("""
            ┌─┐
            │ │
            └─┘
            """, hal.renderFramebuffer());
        assertFalse(hal.framebufferDirty());

        hal.draw((byte) 0, (byte) 0, true);
        assertTrue(hal.framebufferDirty());
        assertEquals("""
            ┌─┐
            │█│
            └─┘
            """, hal.renderFramebuffer());
        assertFalse(hal.framebufferDirty());
    }

    @Test void key() {
        FramebufferHal hal = new FramebufferHal(1, 1, character -> (short) 0);
        assertTrue(hal.getKey().isEmpty());

        hal.keyDown((byte) 6);
        assertEquals(Optional.of((byte) 6), hal.getKey());

        hal.keyUp();
        assertTrue(hal.getKey().isEmpty());
    }
}