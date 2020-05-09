package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
import dev.krk.emulator.chipsekiz.opcodes.Op1NNN;
import dev.krk.emulator.chipsekiz.vm.VM;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ExecutorTest {
    @Test void execute_1NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int address = 0; address <= 0xFFF; address++) {
            executor.execute(vm, hal, new Op1NNN(address));
            int pc = vm.getPC();

            assertEquals(address, pc, Integer.toString(address));
        }

        Mockito.verifyNoInteractions(hal);
    }
}
