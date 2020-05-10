package dev.krk.emulator.chipsekiz.executor;

import dev.krk.emulator.chipsekiz.interpreter.IHal;
import dev.krk.emulator.chipsekiz.opcodes.Op00E0;
import dev.krk.emulator.chipsekiz.opcodes.Op00EE;
import dev.krk.emulator.chipsekiz.opcodes.Op0NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op1NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op2NNN;
import dev.krk.emulator.chipsekiz.opcodes.Op3XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op4XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op5XY0;
import dev.krk.emulator.chipsekiz.opcodes.Op6XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op7XNN;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY0;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY1;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY2;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY3;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY4;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY5;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY6;
import dev.krk.emulator.chipsekiz.opcodes.Op8XY7;
import dev.krk.emulator.chipsekiz.opcodes.Op8XYE;
import dev.krk.emulator.chipsekiz.opcodes.Op9XY0;
import dev.krk.emulator.chipsekiz.opcodes.OpANNN;
import dev.krk.emulator.chipsekiz.opcodes.OpBNNN;
import dev.krk.emulator.chipsekiz.opcodes.OpCXNN;
import dev.krk.emulator.chipsekiz.opcodes.OpFX15;
import dev.krk.emulator.chipsekiz.opcodes.OpFX18;
import dev.krk.emulator.chipsekiz.vm.VM;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        for (short address = 0; address <= 0xFFF; address++) {
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

        for (short address = 0; address <= 0xFFF; address++) {
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

        for (short address = 0; address <= 0xFFF; address++) {
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
                executor.execute(vm, hal, new Op3XNN(vx, (byte) imm));

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
                executor.execute(vm, hal, new Op4XNN(vx, (byte) imm));

                assertEquals(imm != magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_5XY0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        int pc = vm.getOrigin();
        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                vm.setPC(pc);
                vm.setRegister(vy, magic);
                executor.execute(vm, hal, new Op5XY0(vx, vy));
                assertEquals(pc + 2, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));

                if (vx != vy) {
                    vm.setPC(pc);
                    vm.setRegister(vy, notMagic);
                    executor.execute(vm, hal, new Op5XY0(vx, vy));
                    assertEquals(pc, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));
                }
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
                executor.execute(vm, hal, new Op6XNN(vx, (byte) imm));
                byte value = vm.getRegister(vx);

                assertEquals(imm, value, String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_7XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = 99;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setRegister(vx, magic);
                executor.execute(vm, hal, new Op7XNN(vx, (byte) imm));
                byte value = vm.getRegister(vx);

                assertEquals((byte) (magic + imm), value,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                vm.setRegister(vy, notMagic);
                executor.execute(vm, hal, new Op8XY0(vx, vy));
                assertEquals(vm.getRegister(vy), notMagic, String.format("vx: %X, vy: %X", vx, vy));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY1() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY1(vx, 8));
                assertEquals(vx == 8 ? (byte) imm : (byte) (magic | imm), vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY2() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY2(vx, 8));
                assertEquals(vx == 8 ? (byte) imm : (byte) (magic & imm), vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY3() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY3(vx, 8));
                assertEquals(vx == 8 ? 0 : (byte) (magic ^ imm), vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY4() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY4(vx, 8));
                int expectedSum = vx == 8 ? (imm + imm) : (magic + imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedSum, vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertTrue(vm.hasCarry() == ((expectedSum & 0x1FF) != (expectedSum & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY5() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY5(vx, 8));
                int expectedDiff = vx == 8 ? 0 : (magic - imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedDiff, vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertTrue(vm.hasCarry() == ((expectedDiff & 0x1FF) == (expectedDiff & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY6() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY6(vx, 8));
                int expected = ((imm & 0xFF) >> 1);

                if (vx != 0xF) {
                    assertEquals((byte) (expected), vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertTrue(vm.hasCarry() == ((imm & 0x1) == 0x1),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XY7() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XY7(vx, 8));
                int expectedDiff = vx == 8 ? 0 : (-magic + imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedDiff, vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertTrue(vm.hasCarry() == ((expectedDiff & 0x1FF) == (expectedDiff & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_8XYE() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, new Op8XYE(vx, 8));
                int expected = ((imm & 0xFF) << 1);

                if (vx != 0xF) {
                    assertEquals((byte) (expected), vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertTrue(vm.hasCarry() == ((imm & 0x100) == 0x100),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_9XY0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        int pc = vm.getOrigin();
        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                vm.setPC(pc);
                vm.setRegister(vy, magic);
                executor.execute(vm, hal, new Op9XY0(vx, vy));
                assertEquals(pc, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));

                if (vx != vy) {
                    vm.setPC(pc);
                    vm.setRegister(vy, notMagic);
                    executor.execute(vm, hal, new Op9XY0(vx, vy));
                    assertEquals(pc + 2, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));
                }
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_ANNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (short address = 0; address <= 0xFFF; address++) {
            executor.execute(vm, hal, new OpANNN(address));

            assertEquals(address, vm.getI(), String.format("address: %X", address));
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_BNNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int imm = 0; imm <= 0xFF; imm++) {
            vm.setRegister(0, (byte) imm);
            for (short address = 0; address <= 0xFFF; address++) {
                if (((address + imm) & 0xFFFF) > vm.getMemorySize() + 2) {
                    short finalAddress = address;
                    assertThrows(IllegalArgumentException.class,
                        () -> executor.execute(vm, hal, new OpBNNN(finalAddress)),
                        String.format("imm: %X, address: %X", imm, address));
                } else {
                    executor.execute(vm, hal, new OpBNNN(address));
                    assertEquals(address + imm, vm.getPC(),
                        String.format("imm: %X, address: %X", imm, address));
                }
            }
        }

        Mockito.verifyNoInteractions(hal);
    }

    @Test void execute_CXNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        Random rand = new Random();

        for (int imm = 0; imm <= 0xFF; imm++) {
            vm.setRegister(5, (byte) imm);

            when(hal.getRand()).thenReturn((byte) rand.nextInt(256));

            executor.execute(vm, hal, new OpCXNN(5, (byte) imm));

            verify(hal).getRand();
            assertTrue(vm.getRegister(5) <= imm,
                String.format("vx: %X, imm: %X", vm.getRegister(5), imm));

            Mockito.reset(hal);
        }
    }

    @Test void execute_FX18() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, new Op6XNN(vx, (byte) imm));
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
                executor.execute(vm, hal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, new OpFX15(vx));

                assertEquals(vm.getDelayTimer(), (byte) imm,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        Mockito.verifyNoInteractions(hal);
    }
}
