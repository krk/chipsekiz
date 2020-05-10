package dev.krk.emulator.chipsekiz.interpreter;

import dev.krk.emulator.chipsekiz.Decoder;
import dev.krk.emulator.chipsekiz.executor.Executor;
import dev.krk.emulator.chipsekiz.executor.IExecutor;
import dev.krk.emulator.chipsekiz.loader.Layout;
import dev.krk.emulator.chipsekiz.loader.Loader;
import dev.krk.emulator.chipsekiz.opcodes.Op00E0;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY4;
import dev.krk.emulator.chipsekiz.opcodes.Opcode;
import dev.krk.emulator.chipsekiz.tracer.ITracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
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
            () -> new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0, new byte[1],
                1, Layout.empty()).tick());

        assertThrows(IllegalArgumentException.class,
            () -> new Interpreter(loader, decoder, executor, hal, Optional.empty(), 1, new byte[1],
                2, Layout.empty()).tick());

        assertDoesNotThrow(
            () -> new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0, new byte[1],
                2, Layout.empty()).tick());
        Mockito.verifyNoInteractions(hal);
    }

    @Test void endOfMemory_NoExecutor_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = mock(IExecutor.class);
        IHal hal = mock(IHal.class);

        Interpreter interpreter =
            new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0, new byte[1], 2,
                Layout.empty());
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

        Interpreter interpreter =
            new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0,
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

    @Test void jumper_NoHal() {
        assertTicks(new byte[] {0x10, 0x00}, 1000);
        assertTicks(new byte[] {0x10, 0x02, 0x10, 0x00}, 1000);
        assertTicks(new byte[] {0x10, 0x03, 0x00, 0x10, 0x00}, 1000);
        assertTicks(new byte[] {0x10, 0x04, 0x00, 0x00, 0x10, 0x00}, 1000);
    }

    @Test void sound_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
            byte[] program = new byte[] {0x65, (byte) imm, (byte) 0xF5, 0x18, 0x10, 0x04};

            Interpreter interpreter =
                new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0, program,
                    program.length, Layout.empty());

            interpreter.tick();
            Mockito.verifyNoInteractions(hal);

            byte timer = (byte) imm;
            while (timer != 0) {
                interpreter.tick();
                verify(hal).sound(true);
                timer--;
            }

            interpreter.tick();
            if (imm != 0) {
                verify(hal).sound(false);
            }

            Mockito.reset(hal);
        }
    }

    @Test void callret() {
        assertTicks(new byte[] {0x20, 0x04, 0x10, 0x00, 0x00, (byte) 0xEE}, 1000);
    }

    @Test public void invalidExecuteData() {
        byte[] program = {(byte) 0x80, 0x0A};

        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = new Executor();
        IHal fbhal = mock(IHal.class);

        Interpreter interpreter =
            new Interpreter(loader, decoder, executor, fbhal, Optional.empty(), 0x200, program,
                0x1000,
                new Layout(Arrays.asList(new Section[] {new Section(0, CharacterSprites.get())})));

        assertThrows(IllegalStateException.class, () -> interpreter.tick());
    }

    @ParameterizedTest @MethodSource("dev.krk.emulator.chipsekiz.Rom#Names")
    public void testRunRoms(String name) throws IOException {
        byte[] program = getClass().getClassLoader().getResourceAsStream(name).readAllBytes();

        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        final int[] instructionCount = {0};
        Optional<ITracer> tracer = Optional.of((address, opcode) -> instructionCount[0]++);

        Interpreter interpreter =
            new Interpreter(loader, decoder, executor, hal, tracer, 0x200, program, 0x1000,
                Layout.empty());

        // Run roms for 600 cycles.
        for (int i = 0; i < 600; i++) {
            interpreter.tick();

            if (interpreter.getStatus() != InterpreterStatus.READY) {
                System.out.println(String
                    .format("%s %s in %d instructions", name, interpreter.getStatus(),
                        instructionCount[0]));
                return;
            }
        }
        System.out.println(String.format("%s status %s", name, interpreter.getStatus()));
    }

    private static void assertTicks(byte[] program, int ticks) {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        Interpreter interpreter =
            new Interpreter(loader, decoder, executor, hal, Optional.empty(), 0, program,
                program.length, Layout.empty());

        for (int i = 0; i < ticks; i++) {
            assertDoesNotThrow(() -> interpreter.tick());
        }

        Mockito.verifyNoInteractions(hal);
    }
}
