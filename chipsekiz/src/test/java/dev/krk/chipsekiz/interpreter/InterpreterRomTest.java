package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.Decoder;
import dev.krk.chipsekiz.hal.Framebuffer;
import dev.krk.chipsekiz.hal.FramebufferHal;
import dev.krk.chipsekiz.loader.Loader;
import dev.krk.chipsekiz.sprites.CharacterSprites;
import dev.krk.chipsekiz.superchip.hal.SuperChipFramebufferHal;
import dev.krk.chipsekiz.vm.VM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class InterpreterRomTest {
    @BeforeEach public void beforeAll() {
        MockitoAnnotations.initMocks(this);
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
}
