package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
import dev.krk.emulator.chipsekiz.opcodes.Op00E0;
import dev.krk.emulator.chipsekiz.opcodes.Op00EE;
import dev.krk.emulator.chipsekiz.opcodes.Op0NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op1NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op2NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op3XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op4XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op6XNN;
import dev.krk.emulator.chipsekiz.opcodes.OpFX15;
import dev.krk.emulator.chipsekiz.opcodes.OpFX18;
import dev.krk.emulator.chipsekiz.vm.VM;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ExecutorTest {
    @Test void execute_00E0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        executor.execute(vm, hal, new Op00E0());

        verify(hal).clearScreen();
    }

    @Test void execute_00EE() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int address = 0; address <= 0xFFF; address++) {
            vm.push(address);
            executor.execute(vm, hal, new Op00EE());
            int pc = vm.getPC();

            assertEquals(address, pc, String.format("address: %X", address));
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_0NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int address = 0; address <= 0xFFF; address++) {
            int ret = vm.getPC();
            executor.execute(vm, hal, new Op0NNN(address));
            int pc = vm.getPC();

            assertEquals(address, pc, String.format("address: %X", address));
            assertEquals(ret, vm.pop(), String.format("address: %X", address));
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_1NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int address = 0; address <= 0xFFF; address++) {
            executor.execute(vm, hal, new Op1NNN(address));
            int pc = vm.getPC();

            assertEquals(address, pc);
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_2NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int address = 0; address <= 0xFFF; address++) {
            int ret = vm.getPC();
            executor.execute(vm, hal, new Op2NNN(address));
            int pc = vm.getPC();

            assertEquals(address, pc, String.format("address: %X", address));
            assertEquals(ret, vm.pop(), String.format("address: %X", address));
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_3XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        int pc = vm.getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setPC(pc);
                executor.execute(vm, hal, new Op3XNN(vx, imm));

                assertEquals(imm == magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_4XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        int pc = vm.getOrigin();
        byte magic = 0x8;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setPC(pc);
                executor.execute(vm, hal, new Op4XNN(vx, imm));

                assertEquals(imm != magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_6XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, new Op6XNN(vx, imm));
                byte value = vm.getRegister(vx);

                assertEquals(imm, value, String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_FX18() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, new Op6XNN(vx, imm));
                executor.execute(vm, hal, new OpFX18(vx));

                assertEquals(vm.hasSound(), imm != 0,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_FX15() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, new Op6XNN(vx, imm));
                executor.execute(vm, hal, new OpFX15(vx));

                assertEquals(vm.getDelayTimer(), (byte) imm,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }
}
