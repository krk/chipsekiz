package dev.krk.emulator.chipsekiz.interpreter;

import dev.krk.emulator.chipsekiz.Decoder;
import dev.krk.emulator.chipsekiz.executor.IExecutor;
import dev.krk.emulator.chipsekiz.loader.Layout;
import dev.krk.emulator.chipsekiz.loader.Loader;
import dev.krk.emulator.chipsekiz.opcodes.Op00E0;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY4;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class InterpreterTest {
    @BeforeEach public void beforeAll() {
        MockitoAnnotations.initMocks(this);
    }

    @Test void emptyPrograms_NoExecutor_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = mock(IExecutor.class);
        IHal hal = mock(IHal.class);

        assertThrows(IllegalArgumentException.class,
            () -> new Interpreter(loader, decoder, executor, hal, 0, new byte[1], 1, Layout.empty())
                .tick());

        assertThrows(IllegalArgumentException.class,
            () -> new Interpreter(loader, decoder, executor, hal, 1, new byte[1], 2, Layout.empty())
                .tick());

        assertDoesNotThrow(
            () -> new Interpreter(loader, decoder, executor, hal, 0, new byte[1], 2, Layout.empty())
                .tick());
        Mockito.verifyNoInteractions(hal);
    }

    @Test void endOfMemory_NoExecutor_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = mock(IExecutor.class);
        IHal hal = mock(IHal.class);

        Interpreter interpreter =
            new Interpreter(loader, decoder, executor, hal, 0, new byte[1], 2, Layout.empty());
        interpreter.tick();

        assertThrows(IllegalArgumentException.class, () -> interpreter.tick());
        Mockito.verifyNoInteractions(hal);
    }

    @Captor ArgumentCaptor<Opcode> opcodeCaptor;

    @Test void program_NoExecutor_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = mock(IExecutor.class);
        IHal hal = mock(IHal.class);

        Interpreter interpreter = new Interpreter(loader, decoder, executor, hal, 0,
            new byte[] {(byte) 0x00, (byte) 0xE0, (byte) 0x80, 0x24}, 4, Layout.empty());

        interpreter.tick();
        verify(executor).execute(any(), any(), opcodeCaptor.capture());
        assertTrue(opcodeCaptor.getValue() instanceof Op00E0);

        Mockito.reset(executor);
        interpreter.tick();
        verify(executor).execute(any(), any(), opcodeCaptor.capture());
        assertTrue(opcodeCaptor.getValue() instanceof Op8XY4);
        assertEquals(Optional.of(0), opcodeCaptor.getValue().getVx());
        assertEquals(Optional.of(2), opcodeCaptor.getValue().getVy());

        Op8XY4 op = (Op8XY4) opcodeCaptor.getValue();
        assertEquals(0, op.vx());
        assertEquals(2, op.vy());

        Mockito.verifyNoInteractions(hal);
    }
}
