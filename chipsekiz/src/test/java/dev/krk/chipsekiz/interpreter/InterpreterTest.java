package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.Decoder;
import dev.krk.chipsekiz.hal.Framebuffer;
import dev.krk.chipsekiz.hal.FramebufferHal;
import dev.krk.chipsekiz.loader.Layout;
import dev.krk.chipsekiz.loader.Loader;
import dev.krk.chipsekiz.opcodes.Op00E0;
import dev.krk.chipsekiz.opcodes.Op8XY4;
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.sprites.CharacterSprites;
import dev.krk.chipsekiz.superchip.hal.SuperChipFramebufferHal;
import dev.krk.chipsekiz.vm.VM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
            () -> new Interpreter(VM::new, loader, decoder, executor, hal, null, 0, new byte[1], 1,
                Layout.empty()).tick());

        assertThrows(IllegalArgumentException.class,
            () -> new Interpreter(VM::new, loader, decoder, executor, hal, null, 1, new byte[1], 2,
                Layout.empty()).tick());

        assertDoesNotThrow(
            () -> new Interpreter(VM::new, loader, decoder, executor, hal, null, 0, new byte[1], 2,
                Layout.empty()).tick());
    }

    @Test void endOfMemory_NoExecutor_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = mock(IExecutor.class);
        IHal hal = mock(IHal.class);

        Interpreter interpreter =
            new Interpreter(VM::new, loader, decoder, executor, hal, null, 0, new byte[1], 2,
                Layout.empty());
        Mockito.reset(hal);

        interpreter.tick();

        assertThrows(IllegalArgumentException.class, interpreter::tick);
        Mockito.verifyNoInteractions(hal);
    }

    @Captor ArgumentCaptor<Opcode> opcodeCaptor;

    @Test void program_NoExecutor_NoHal() {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IExecutor executor = mock(IExecutor.class);
        IHal hal = mock(IHal.class);

        Interpreter interpreter = new Interpreter(VM::new, loader, decoder, executor, hal, null, 0,
            new byte[] {(byte) 0x00, (byte) 0xE0, (byte) 0x80, 0x24}, 4, Layout.empty());
        Mockito.reset(hal);

        interpreter.tick();
        verify(executor).execute(opcodeCaptor.capture());
        assertTrue(opcodeCaptor.getValue() instanceof Op00E0);

        Mockito.reset(executor);
        interpreter.tick();
        verify(executor).execute(opcodeCaptor.capture());
        assertTrue(opcodeCaptor.getValue() instanceof Op8XY4);
        assertEquals(0, opcodeCaptor.getValue().getVx());
        assertEquals(2, opcodeCaptor.getValue().getVy());

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
        IHal hal = mock(IHal.class);
        IExecutor executor = new Executor(hal, null);

        for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
            byte[] program = new byte[] {0x65, (byte) imm, (byte) 0xF5, 0x18, 0x10, 0x04};

            Interpreter interpreter =
                new Interpreter(VM::new, loader, decoder, executor, hal, null, 0, program,
                    program.length, Layout.empty());
            Mockito.reset(hal);

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
        IHal fbhal = mock(IHal.class);
        IExecutor executor = new Executor(fbhal, null);


        Interpreter interpreter =
            new Interpreter(VM::new, loader, decoder, executor, fbhal, null, 0x200, program, 0x1000,
                CharacterSprites.DefaultLayout());

        assertThrows(IllegalStateException.class, interpreter::tick);
    }

    @ParameterizedTest @MethodSource("dev.krk.chipsekiz.Rom#Names")
    public void testRunRoms(String name) throws IOException {
        byte[] program = getClass().getClassLoader().getResourceAsStream(name).readAllBytes();

        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        Framebuffer fb = new Framebuffer(64, 32);
        FramebufferHal fbhal = new FramebufferHal(fb);
        IExecutor executor = new Executor(fbhal, CharacterSprites.getAddressLocator());

        final int[] instructionCount = {0};

        Interpreter interpreter = new Interpreter(VM::new, loader, decoder, executor, fbhal,
            (address, opcode) -> instructionCount[0]++, 0x200, program, 0x1000,
            CharacterSprites.DefaultLayout());

        // Run roms for 600 cycles.
        for (int i = 0; i < 600; i++) {
            interpreter.tick();

            if (interpreter.getStatus() != InterpreterStatus.READY) {
                break;
            }
        }
        System.out.println(String
            .format("%s status %s in %d instructions\n%s", name, interpreter.getStatus(),
                instructionCount[0], fbhal.renderFramebuffer()));
    }

    @ParameterizedTest @MethodSource("dev.krk.chipsekiz.Rom#SuperChipNames")
    public void testRunSuperChip8Roms(String name) throws IOException {
        byte[] program = getClass().getClassLoader().getResourceAsStream(name).readAllBytes();

        SuperChipFramebufferHal hal = new SuperChipFramebufferHal();

        final int[] instructionCount = {0};
        IInterpreter interpreter = InterpreterFactory
            .createSuperChip(hal, (address, opcode) -> instructionCount[0]++, null);
        interpreter.load(0x200, program);

        // Run roms for 20000 cycles.
        for (int i = 0; i < 20000; i++) {
            interpreter.tick();

            if (interpreter.getStatus() != InterpreterStatus.READY) {
                break;
            }
        }
        System.out.println(String
            .format("%s status %s in %d instructions\n%s", name, interpreter.getStatus(),
                instructionCount[0], hal.renderFramebuffer()));
    }

    private static void assertTicks(byte[] program, int ticks) {
        Loader loader = new Loader();
        Decoder decoder = new Decoder();
        IHal hal = mock(IHal.class);
        IExecutor executor = new Executor(hal, null);


        Interpreter interpreter =
            new Interpreter(VM::new, loader, decoder, executor, hal, null, 0, program,
                program.length, Layout.empty());
        Mockito.reset(hal);

        for (int i = 0; i < ticks; i++) {
            assertDoesNotThrow(interpreter::tick);
        }

        Mockito.verifyNoInteractions(hal);
    }
}
