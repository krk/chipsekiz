package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.opcodes.Op7XNN;
import dev.krk.chipsekiz.opcodes.Op8XY1;
import dev.krk.chipsekiz.opcodes.Op8XY2;
import dev.krk.chipsekiz.opcodes.Op8XY3;
import dev.krk.chipsekiz.opcodes.Op8XY4;
import dev.krk.chipsekiz.opcodes.Op8XY5;
import dev.krk.chipsekiz.opcodes.Op8XY6;
import dev.krk.chipsekiz.opcodes.Op8XY7;
import dev.krk.chipsekiz.opcodes.Op8XYE;
import dev.krk.chipsekiz.opcodes.OpEXA1;
import dev.krk.chipsekiz.opcodes.OpFX07;
import dev.krk.chipsekiz.opcodes.OpFX0A;
import dev.krk.chipsekiz.opcodes.Op00E0;
import dev.krk.chipsekiz.opcodes.Op00EE;
import dev.krk.chipsekiz.opcodes.Op0NNN;
import dev.krk.chipsekiz.opcodes.Op1NNN;
import dev.krk.chipsekiz.opcodes.Op2NNN;
import dev.krk.chipsekiz.opcodes.Op3XNN;
import dev.krk.chipsekiz.opcodes.Op4XNN;
import dev.krk.chipsekiz.opcodes.Op5XY0;
import dev.krk.chipsekiz.opcodes.Op6XNN;
import dev.krk.chipsekiz.opcodes.Op8XY0;
import dev.krk.chipsekiz.opcodes.Op9XY0;
import dev.krk.chipsekiz.opcodes.OpANNN;
import dev.krk.chipsekiz.opcodes.OpBNNN;
import dev.krk.chipsekiz.opcodes.OpCXNN;
import dev.krk.chipsekiz.opcodes.OpDXYN;
import dev.krk.chipsekiz.opcodes.OpEX9E;
import dev.krk.chipsekiz.opcodes.OpFX15;
import dev.krk.chipsekiz.opcodes.OpFX18;
import dev.krk.chipsekiz.opcodes.OpFX1E;
import dev.krk.chipsekiz.opcodes.OpFX29;
import dev.krk.chipsekiz.opcodes.OpFX33;
import dev.krk.chipsekiz.opcodes.OpFX55;
import dev.krk.chipsekiz.opcodes.OpFX65;
import dev.krk.chipsekiz.sprites.CharacterSprites;
import dev.krk.chipsekiz.vm.VM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.google.common.base.Strings.padStart;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ExecutorTest {
    @Test void execute_00E0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        executor.execute(vm, hal, cal, new Op00E0());

        verify(hal).clearScreen();
        verifyNoInteractions(cal);
    }

    @Test void execute_00EE() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int address = 0; address <= 0xFFF; address++) {
            vm.push(address);
            executor.execute(vm, hal, cal, new Op00EE());
            int pc = vm.getPC();

            assertEquals(address, pc, String.format("address: %X", address));
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_0NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (short address = 0; address <= 0xFFF; address++) {
            int ret = vm.getPC();
            executor.execute(vm, hal, cal, new Op0NNN(address));
            int pc = vm.getPC();

            assertEquals(ret, pc, String.format("address: %X", address));
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_1NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (short address = 0; address <= 0xFFF; address++) {
            executor.execute(vm, hal, cal, new Op1NNN(address));
            int pc = vm.getPC();

            assertEquals(address, pc);
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_2NNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (short address = 0; address <= 0xFFF; address++) {
            int ret = vm.getPC();
            executor.execute(vm, hal, cal, new Op2NNN(address));
            int pc = vm.getPC();

            assertEquals(address, pc, String.format("address: %X", address));
            assertEquals(ret, vm.pop(), String.format("address: %X", address));
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_3XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        int pc = vm.getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setPC(pc);
                executor.execute(vm, hal, cal, new Op3XNN(vx, (byte) imm));

                assertEquals(imm == magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_4XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        int pc = vm.getOrigin();
        byte magic = 0x8;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setPC(pc);
                executor.execute(vm, hal, cal, new Op4XNN(vx, (byte) imm));

                assertEquals(imm != magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_5XY0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        int pc = vm.getOrigin();
        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                vm.setPC(pc);
                vm.setRegister(vy, magic);
                executor.execute(vm, hal, cal, new Op5XY0(vx, vy));
                assertEquals(pc + 2, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));

                if (vx != vy) {
                    vm.setPC(pc);
                    vm.setRegister(vy, notMagic);
                    executor.execute(vm, hal, cal, new Op5XY0(vx, vy));
                    assertEquals(pc, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));
                }
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_6XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                byte value = vm.getRegister(vx);

                assertEquals(imm, value, String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_7XNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = 99;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setRegister(vx, magic);
                executor.execute(vm, hal, cal, new Op7XNN(vx, (byte) imm));
                byte value = vm.getRegister(vx);

                assertEquals((byte) (magic + imm), value,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                vm.setRegister(vy, notMagic);
                executor.execute(vm, hal, cal, new Op8XY0(vx, vy));
                assertEquals(vm.getRegister(vy), notMagic, String.format("vx: %X, vy: %X", vx, vy));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY1() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY1(vx, 8));
                assertEquals(vx == 8 ? (byte) imm : (byte) (magic | imm), vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY2() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY2(vx, 8));
                assertEquals(vx == 8 ? (byte) imm : (byte) (magic & imm), vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY3() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY3(vx, 8));
                assertEquals(vx == 8 ? 0 : (byte) (magic ^ imm), vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY4() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY4(vx, 8));
                int expectedSum = vx == 8 ? (imm + imm) : (magic + imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedSum, vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertEquals(vm.hasCarry(), ((expectedSum & 0x1FF) != (expectedSum & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY5() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY5(vx, 8));
                int expectedDiff = vx == 8 ? 0 : (magic - imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedDiff, vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertEquals(vm.hasCarry(), ((expectedDiff & 0x1FF) == (expectedDiff & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @ParameterizedTest @ValueSource(booleans = {false, true}) void execute_8XY6(
        boolean bitShiftsIgnoreVY) {
        VM vm = new VM();
        IExecutor executor = new Executor(bitShiftsIgnoreVY);
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, (byte) (~imm & 0xFF));
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY6(vx, 8));

                if (vx != 0xF) {
                    if (bitShiftsIgnoreVY) {
                        int expectedNoVY = ((vx == 8 ? imm : ~imm) & 0xFF) >> 1;
                        assertEquals((byte) expectedNoVY, vm.getRegister(vx),
                            String.format("vx: %X, imm: %X", vx, imm));
                        assertEquals(vm.hasCarry(), ((vx == 8 ? imm : ~imm) & 0x1) == 0x1,
                            String.format("vx: %X, imm: %X", vx, imm));
                    } else {
                        int expected = (imm & 0xFF) >> 1;
                        assertEquals((byte) expected, vm.getRegister(vx),
                            String.format("vx: %X, imm: %X", vx, imm));
                        assertEquals(vm.hasCarry(), (imm & 0x1) == 0x1,
                            String.format("vx: %X, imm: %X", vx, imm));
                    }
                }
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_8XY7() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, magic);
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XY7(vx, 8));
                int expectedDiff = vx == 8 ? 0 : (-magic + imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedDiff, vm.getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertEquals(vm.hasCarry(), ((expectedDiff & 0x1FF) == (expectedDiff & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @ParameterizedTest @ValueSource(booleans = {false, true}) void execute_8XYE(
        boolean bitShiftsIgnoreVY) {
        VM vm = new VM();
        IExecutor executor = new Executor(bitShiftsIgnoreVY);
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                vm.setRegister(vx, (byte) (~imm & 0xFF));
                vm.setRegister(8, (byte) imm);
                executor.execute(vm, hal, cal, new Op8XYE(vx, 8));


                if (vx != 0xF) {
                    if (bitShiftsIgnoreVY) {
                        int expected = ((vx == 8 ? imm : ~imm) & 0xFF) << 1;

                        assertEquals((byte) (expected), vm.getRegister(vx),
                            String.format("vx: %X, imm: %X", vx, imm));
                        assertEquals(vm.hasCarry(), ((expected & 0x100) == 0x100),
                            String.format("vx: %X, imm: %X", vx, imm));
                    } else {
                        int expected = ((imm & 0xFF) << 1);

                        assertEquals((byte) (expected), vm.getRegister(vx),
                            String.format("vx: %X, imm: %X", vx, imm));
                        assertEquals(vm.hasCarry(), ((expected & 0x100) == 0x100),
                            String.format("vx: %X, imm: %X", vx, imm));
                    }
                }
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_9XY0() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        int pc = vm.getOrigin();
        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                vm.setPC(pc);
                vm.setRegister(vy, magic);
                executor.execute(vm, hal, cal, new Op9XY0(vx, vy));
                assertEquals(pc, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));

                if (vx != vy) {
                    vm.setPC(pc);
                    vm.setRegister(vy, notMagic);
                    executor.execute(vm, hal, cal, new Op9XY0(vx, vy));
                    assertEquals(pc + 2, vm.getPC(), String.format("vx: %X, vy: %X", vx, vy));
                }
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_ANNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (short address = 0; address <= 0xFFF; address++) {
            executor.execute(vm, hal, cal, new OpANNN(address));

            assertEquals(address, vm.getI(), String.format("address: %X", address));
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_BNNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int imm = 0; imm <= 0xFF; imm++) {
            vm.setRegister(0, (byte) imm);
            for (short address = 0; address <= 0xFFF; address++) {
                if (((address + imm) & 0xFFFF) > vm.getMemorySize() + 2) {
                    short finalAddress = address;
                    assertThrows(IllegalArgumentException.class,
                        () -> executor.execute(vm, hal, cal, new OpBNNN(finalAddress)),
                        String.format("imm: %X, address: %X", imm, address));
                } else {
                    executor.execute(vm, hal, cal, new OpBNNN(address));
                    assertEquals(address + imm, vm.getPC(),
                        String.format("imm: %X, address: %X", imm, address));
                }
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_CXNN() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        Random rand = new Random();

        for (int imm = 0; imm <= 0xFF; imm++) {
            vm.setRegister(5, (byte) imm);

            when(hal.getRand()).thenReturn((byte) rand.nextInt(256));

            executor.execute(vm, hal, cal, new OpCXNN(5, (byte) imm));

            verify(hal).getRand();
            assertTrue(vm.getRegister(5) <= imm,
                String.format("vx: %X, imm: %X", vm.getRegister(5), imm));

            Mockito.reset(hal);
        }

        verifyNoInteractions(cal);
    }

    @Test void execute_DXYN() {
        final byte[] bitmap =
            {(byte) 0xA5, 0x20, (byte) 0xFE, 0x41, (byte) 0x96, 0x57, 0x10, 0x00, (byte) 0xFF, 0x56,
                (byte) 0x90, 0x04, 0x06, 0x25, 0x5A};

        final byte[] memory = new byte[0x300];
        System.arraycopy(bitmap, 0, memory, 0x242, bitmap.length);

        VM vm = new VM(0x200, memory);
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        vm.setI((short) 0x242);

        byte coordX = 0x10;
        byte coordY = 0x15;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int vy = 0; vy <= 0xF; vy++) {
                for (byte n = 0; n <= 0xF; n++) {
                    vm.setRegister(vx, coordX);
                    vm.setRegister(vy, coordY);

                    Mockito.reset(hal);

                    executor.execute(vm, hal, cal, new OpDXYN(vx, vy, n));

                    if (n == 0) {
                        verifyNoInteractions(hal);
                        continue;
                    }

                    ArgumentCaptor<Byte> drawXCaptor = ArgumentCaptor.forClass(Byte.class);
                    ArgumentCaptor<Byte> drawYCaptor = ArgumentCaptor.forClass(Byte.class);
                    ArgumentCaptor<Boolean> drawNCaptor = ArgumentCaptor.forClass(Boolean.class);

                    verify(hal, times(n * 8))
                        .draw(drawXCaptor.capture(), drawYCaptor.capture(), drawNCaptor.capture());

                    List<Byte> xs = drawXCaptor.getAllValues();
                    List<Byte> ys = drawYCaptor.getAllValues();
                    List<Boolean> ns = drawNCaptor.getAllValues();

                    int i = 0;
                    for (byte dy = 0; dy < n; dy++) {
                        for (byte dx = 0; dx < 8; dx++) {
                            byte row = (byte) (coordY + dy);
                            byte col =
                                vx == vy ? (byte) (coordY + 7 - dx) : (byte) (coordX + 7 - dx);

                            String message =
                                String.format("vx: %d vy: %d dy: %d dx: %d", vx, vy, dy, dx);

                            assertEquals(col, xs.get(i), message);
                            assertEquals(row, ys.get(i), message);
                            assertEquals((bitmap[dy] & (1 << dx)) == 1 << dx, ns.get(i), message);
                            i++;
                        }
                    }
                }
            }
        }

        verifyNoInteractions(cal);
    }

    @Test void execute_EX9E() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        int pc = vm.getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            vm.setPC(pc);

            executor.execute(vm, hal, cal, new OpEX9E(vx));
            assertEquals(pc, vm.getPC(), String.format("vx: %X", vx));

            for (byte key = 0; key <= 0xF; key++) {
                vm.setPC(pc);
                when(hal.getKey()).thenReturn(Optional.of(key));

                executor.execute(vm, hal, cal, new OpEX9E(vx));
                assertEquals(key == magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X key :%X", vx, key));
            }
        }

        verifyNoInteractions(cal);
    }

    @Test void execute_EXA1() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        int pc = vm.getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setRegister(vx, magic);
            vm.setPC(pc);

            executor.execute(vm, hal, cal, new OpEXA1(vx));
            assertEquals(pc + 2, vm.getPC(), String.format("vx: %X", vx));

            for (byte key = 0; key <= 0xF; key++) {
                vm.setPC(pc);
                when(hal.getKey()).thenReturn(Optional.of(key));

                executor.execute(vm, hal, cal, new OpEXA1(vx));
                assertEquals(key != magic ? pc + 2 : pc, vm.getPC(),
                    String.format("vx: %X key :%X", vx, key));
            }
        }

        verifyNoInteractions(cal);
    }

    @Test void execute_FX07() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                vm.setDelayTimer((byte) imm);
                executor.execute(vm, hal, cal, new OpFX07(vx));

                assertEquals(imm, vm.getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_FX0A() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        int pc = vm.getOrigin();

        for (int vx = 0; vx <= 0xF; vx++) {
            vm.setPC(pc);
            when(hal.getKey()).thenReturn(Optional.empty());

            executor.execute(vm, hal, cal, new OpFX0A(vx));

            assertEquals(pc - 2, vm.getPC(), String.format("vx: %X", vx));

            for (byte key = 0; key <= 0xF; key++) {
                vm.setPC(pc);
                when(hal.getKey()).thenReturn(Optional.of(key));

                executor.execute(vm, hal, cal, new OpFX0A(vx));
                assertEquals(pc, vm.getPC(), String.format("vx: %X, key: %X", vx, key));
                assertEquals(key, vm.getRegister(vx), String.format("vx: %X, key: %X", vx, key));
            }
        }

        verifyNoInteractions(cal);
    }

    @Test void execute_FX15() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, cal, new OpFX15(vx));

                assertEquals(vm.getDelayTimer(), (byte) imm,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_FX18() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, cal, new OpFX18(vx));

                assertEquals(vm.hasSound(), imm != 0,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_FX1E() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                short I = vm.getI();
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, cal, new OpFX1E(vx));

                assertEquals((short) (I + imm), vm.getI(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_FX29() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (byte imm = 0; imm <= 0xF; imm++) {
                when(cal.getCharacterAddress(anyByte())).then(
                    invocationOnMock -> CharacterSprites.getAddressLocator()
                        .getCharacterAddress(invocationOnMock.getArgument(0)));

                executor.execute(vm, hal, cal, new Op6XNN(vx, imm));
                executor.execute(vm, hal, cal, new OpFX29(vx));

                assertEquals(CharacterSprites.getAddressLocator().getCharacterAddress(imm),
                    vm.getI(), String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(hal);
    }

    @Test void execute_FX33() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                short I = vm.getI();
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, cal, new OpFX33(vx));

                String num = padStart(Integer.toString(Byte.toUnsignedInt((byte) imm)), 3, '0');

                String message = String.format("vx: %X, imm: %X", vx, (byte) imm);
                assertEquals(num.charAt(0) - '0', vm.getByte(I), message);
                assertEquals(num.charAt(1) - '0', vm.getByte(I + 1), message);
                assertEquals(num.charAt(2) - '0', vm.getByte(I + 2), message);
            }
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_FX55() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        vm.setI((short) 0x526);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, cal, new OpFX55(vx));

                short I = vm.getI();
                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : 0xC0 | x * 2 + 1), vm.getByte(I + x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }
            }
            vm.setRegister(vx, (byte) (0xC0 | (vx * 2 + 1)));
        }

        verifyNoInteractions(hal);
        verifyNoInteractions(cal);
    }

    @Test void execute_FX65() {
        VM vm = new VM();
        IExecutor executor = new Executor();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        vm.setI((short) 0x526);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                executor.execute(vm, hal, cal, new Op6XNN(vx, (byte) imm));
                executor.execute(vm, hal, cal, new OpFX55(vx));
                for (byte x = 0; x <= vx; x++) {
                    vm.setRegister(x, (byte) 0x00);
                }

                executor.execute(vm, hal, cal, new OpFX65(vx));
                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : 0xB0 | x * 2 + 1), vm.getRegister(x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }

                executor.execute(vm, hal, cal, new OpFX65(vx));
                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : 0xB0 | x * 2 + 1), vm.getRegister(x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }
            }
            vm.setRegister(vx, (byte) (0xB0 | (vx * 2 + 1)));
        }

        verifyNoInteractions(hal);
    }
}
