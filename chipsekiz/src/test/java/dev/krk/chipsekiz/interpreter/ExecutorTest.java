package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.opcodes.Op00E0;
import dev.krk.chipsekiz.opcodes.Op00EE;
import dev.krk.chipsekiz.opcodes.Op0NNN;
import dev.krk.chipsekiz.opcodes.Op1NNN;
import dev.krk.chipsekiz.opcodes.Op2NNN;
import dev.krk.chipsekiz.opcodes.Op3XNN;
import dev.krk.chipsekiz.opcodes.Op4XNN;
import dev.krk.chipsekiz.opcodes.Op5XY0;
import dev.krk.chipsekiz.opcodes.Op6XNN;
import dev.krk.chipsekiz.opcodes.Op7XNN;
import dev.krk.chipsekiz.opcodes.Op8XY0;
import dev.krk.chipsekiz.opcodes.Op8XY1;
import dev.krk.chipsekiz.opcodes.Op8XY2;
import dev.krk.chipsekiz.opcodes.Op8XY3;
import dev.krk.chipsekiz.opcodes.Op8XY4;
import dev.krk.chipsekiz.opcodes.Op8XY5;
import dev.krk.chipsekiz.opcodes.Op8XY6;
import dev.krk.chipsekiz.opcodes.Op8XY7;
import dev.krk.chipsekiz.opcodes.Op8XYE;
import dev.krk.chipsekiz.opcodes.Op9XY0;
import dev.krk.chipsekiz.opcodes.OpANNN;
import dev.krk.chipsekiz.opcodes.OpBNNN;
import dev.krk.chipsekiz.opcodes.OpCXNN;
import dev.krk.chipsekiz.opcodes.OpDXYN;
import dev.krk.chipsekiz.opcodes.OpEX9E;
import dev.krk.chipsekiz.opcodes.OpEXA1;
import dev.krk.chipsekiz.opcodes.OpFX07;
import dev.krk.chipsekiz.opcodes.OpFX0A;
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


public class ExecutorTest {
    protected ExecutorTestContext createExecutorContext(boolean bitShiftsIgnoreVY,
        boolean loadDumpIncreasesI) {
        VM vm = new VM();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        IExecutor executor = new Executor(vm, hal, cal, bitShiftsIgnoreVY, loadDumpIncreasesI);

        return new ExecutorTestContext(vm, hal, cal, executor);
    }

    protected ExecutorTestContext createExecutorContext(int origin, byte[] memory) {
        VM vm = new VM(origin, memory);
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        IExecutor executor = new Executor(vm, hal, cal);

        return new ExecutorTestContext(vm, hal, cal, executor);
    }

    protected ExecutorTestContext createExecutorContext() {
        VM vm = new VM();
        IHal hal = mock(IHal.class);
        ICharacterAddressLocator cal = mock(ICharacterAddressLocator.class);
        IExecutor executor = new Executor(vm, hal, cal);

        return new ExecutorTestContext(vm, hal, cal, executor);
    }

    @Test void execute_00E0() {
        ExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00E0());

        verify(ctx.hal()).clearScreen();
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_00EE() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int address = 0; address <= 0xFFF; address++) {
            ctx.vm().push(address);
            ctx.executor().execute(new Op00EE());
            int pc = ctx.vm().getPC();

            assertEquals(address, pc, String.format("address: %X", address));
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_0NNN() {
        ExecutorTestContext ctx = createExecutorContext();

        for (short address = 0; address <= 0xFFF; address++) {
            int ret = ctx.vm().getPC();
            ctx.executor().execute(new Op0NNN(address));
            int pc = ctx.vm().getPC();

            assertEquals(ret, pc, String.format("address: %X", address));
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_1NNN() {
        ExecutorTestContext ctx = createExecutorContext();

        for (short address = 0; address <= 0xFFF; address++) {
            ctx.executor().execute(new Op1NNN(address));
            int pc = ctx.vm().getPC();

            assertEquals(address, pc);
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_2NNN() {
        ExecutorTestContext ctx = createExecutorContext();

        for (short address = 0; address <= 0xFFF; address++) {
            int ret = ctx.vm().getPC();
            ctx.executor().execute(new Op2NNN(address));
            int pc = ctx.vm().getPC();

            assertEquals(address, pc, String.format("address: %X", address));
            assertEquals(ret, ctx.vm().pop(), String.format("address: %X", address));
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_3XNN() {
        ExecutorTestContext ctx = createExecutorContext();

        int pc = ctx.vm().getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.vm().setPC(pc);
                ctx.executor().execute(new Op3XNN(vx, (byte) imm));

                assertEquals(imm == magic ? pc + 2 : pc, ctx.vm().getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_4XNN() {
        ExecutorTestContext ctx = createExecutorContext();

        int pc = ctx.vm().getOrigin();
        byte magic = 0x8;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.vm().setPC(pc);
                ctx.executor().execute(new Op4XNN(vx, (byte) imm));

                assertEquals(imm != magic ? pc + 2 : pc, ctx.vm().getPC(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_5XY0() {
        ExecutorTestContext ctx = createExecutorContext();

        int pc = ctx.vm().getOrigin();
        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                ctx.vm().setPC(pc);
                ctx.vm().setRegister(vy, magic);
                ctx.executor().execute(new Op5XY0(vx, vy));
                assertEquals(pc + 2, ctx.vm().getPC(), String.format("vx: %X, vy: %X", vx, vy));

                if (vx != vy) {
                    ctx.vm().setPC(pc);
                    ctx.vm().setRegister(vy, notMagic);
                    ctx.executor().execute(new Op5XY0(vx, vy));
                    assertEquals(pc, ctx.vm().getPC(), String.format("vx: %X, vy: %X", vx, vy));
                }
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_6XNN() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                byte value = ctx.vm().getRegister(vx);

                assertEquals(imm, value, String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_7XNN() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = 99;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.executor().execute(new Op7XNN(vx, (byte) imm));
                byte value = ctx.vm().getRegister(vx);

                assertEquals((byte) (magic + imm), value,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY0() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                ctx.vm().setRegister(vy, notMagic);
                ctx.executor().execute(new Op8XY0(vx, vy));
                assertEquals(ctx.vm().getRegister(vy), notMagic,
                    String.format("vx: %X, vy: %X", vx, vy));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY1() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY1(vx, 8));
                assertEquals(vx == 8 ? (byte) imm : (byte) (magic | imm), ctx.vm().getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY2() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY2(vx, 8));
                assertEquals(vx == 8 ? (byte) imm : (byte) (magic & imm), ctx.vm().getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY3() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY3(vx, 8));
                assertEquals(vx == 8 ? 0 : (byte) (magic ^ imm), ctx.vm().getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY4() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY4(vx, 8));
                int expectedSum = vx == 8 ? (imm + imm) : (magic + imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedSum, ctx.vm().getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertEquals(ctx.vm().hasCarry(), ((expectedSum & 0x1FF) != (expectedSum & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY5() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY5(vx, 8));
                int expectedDiff = vx == 8 ? 0 : (magic - imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedDiff, ctx.vm().getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertEquals(ctx.vm().hasCarry(), ((expectedDiff & 0x1FF) == (expectedDiff & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @ParameterizedTest @ValueSource(booleans = {false, true}) void execute_8XY6(
        boolean bitShiftsIgnoreVY) {
        ExecutorTestContext ctx = createExecutorContext(bitShiftsIgnoreVY, false);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, (byte) (~imm & 0xFF));
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY6(vx, 8));

                if (vx != 0xF) {
                    int expected = bitShiftsIgnoreVY ?
                        ((vx == 8 ? imm : ~imm) & 0xFF) >> 1 :
                        (imm & 0xFF) >> 1;
                    boolean hasCarry = bitShiftsIgnoreVY ?
                        ((vx == 8 ? imm : ~imm) & 0x1) == 0x1 :
                        (imm & 0x1) == 0x1;

                    assertEquals((byte) expected, ctx.vm().getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                    assertEquals(ctx.vm().hasCarry(), hasCarry,
                        String.format("vx: %X, imm: %X", vx, imm));
                }
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_8XY7() {
        ExecutorTestContext ctx = createExecutorContext();

        byte magic = (byte) 0x7;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, magic);
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XY7(vx, 8));
                int expectedDiff = vx == 8 ? 0 : (-magic + imm);

                if (vx != 0xF) {
                    assertEquals((byte) expectedDiff, ctx.vm().getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                }

                assertEquals(ctx.vm().hasCarry(), ((expectedDiff & 0x1FF) == (expectedDiff & 0xFF)),
                    String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @ParameterizedTest @ValueSource(booleans = {false, true}) void execute_8XYE(
        boolean bitShiftsIgnoreVY) {
        ExecutorTestContext ctx = createExecutorContext(bitShiftsIgnoreVY, false);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = 0; imm <= 0xFF; imm++) {
                ctx.vm().setRegister(vx, (byte) (~imm & 0xFF));
                ctx.vm().setRegister(8, (byte) imm);
                ctx.executor().execute(new Op8XYE(vx, 8));

                if (vx != 0xF) {
                    int expected = bitShiftsIgnoreVY ?
                        ((vx == 8 ? imm : ~imm) & 0xFF) << 1 :
                        (imm & 0xFF) << 1;

                    assertEquals((byte) (expected), ctx.vm().getRegister(vx),
                        String.format("vx: %X, imm: %X", vx, imm));
                    assertEquals(ctx.vm().hasCarry(), ((expected & 0x100) == 0x100),
                        String.format("vx: %X, imm: %X", vx, imm));
                }
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_9XY0() {
        ExecutorTestContext ctx = createExecutorContext();

        int pc = ctx.vm().getOrigin();
        byte magic = 0x8;
        byte notMagic = 0x6;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            for (int vy = 0; vy <= 0xF; vy++) {
                ctx.vm().setPC(pc);
                ctx.vm().setRegister(vy, magic);
                ctx.executor().execute(new Op9XY0(vx, vy));
                assertEquals(pc, ctx.vm().getPC(), String.format("vx: %X, vy: %X", vx, vy));

                if (vx != vy) {
                    ctx.vm().setPC(pc);
                    ctx.vm().setRegister(vy, notMagic);
                    ctx.executor().execute(new Op9XY0(vx, vy));
                    assertEquals(pc + 2, ctx.vm().getPC(), String.format("vx: %X, vy: %X", vx, vy));
                }
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_ANNN() {
        ExecutorTestContext ctx = createExecutorContext();

        for (short address = 0; address <= 0xFFF; address++) {
            ctx.executor().execute(new OpANNN(address));

            assertEquals(address, ctx.vm().getI(), String.format("address: %X", address));
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_BNNN() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int imm = 0; imm <= 0xFF; imm++) {
            ctx.vm().setRegister(0, (byte) imm);
            for (short address = 0; address <= 0xFFF; address++) {
                if (((address + imm) & 0xFFFF) > ctx.vm().getMemorySize() + 2) {
                    short finalAddress = address;
                    assertThrows(IllegalArgumentException.class,
                        () -> ctx.executor().execute(new OpBNNN(finalAddress)),
                        String.format("imm: %X, address: %X", imm, address));
                } else {
                    ctx.executor().execute(new OpBNNN(address));
                    assertEquals(address + imm, ctx.vm().getPC(),
                        String.format("imm: %X, address: %X", imm, address));
                }
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_CXNN() {
        ExecutorTestContext ctx = createExecutorContext();
        Random rand = new Random();

        for (int imm = 0; imm <= 0xFF; imm++) {
            ctx.vm().setRegister(5, (byte) imm);

            when(ctx.hal().getRand()).thenReturn((byte) rand.nextInt(256));

            ctx.executor().execute(new OpCXNN(5, (byte) imm));

            verify(ctx.hal()).getRand();
            assertTrue(ctx.vm().getRegister(5) <= imm,
                String.format("vx: %X, imm: %X", ctx.vm().getRegister(5), imm));

            Mockito.reset(ctx.hal());
        }

        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_DXYN() {
        final byte[] bitmap =
            {(byte) 0xA5, 0x20, (byte) 0xFE, 0x41, (byte) 0x96, 0x57, 0x10, 0x00, (byte) 0xFF, 0x56,
                (byte) 0x90, 0x04, 0x06, 0x25, 0x5A};

        final byte[] memory = new byte[0x300];
        System.arraycopy(bitmap, 0, memory, 0x242, bitmap.length);

        ExecutorTestContext ctx = createExecutorContext(0x200, memory);

        ctx.vm().setI((short) 0x242);

        byte coordX = 0x10;
        byte coordY = 0x15;
        for (int vx = 0; vx <= 0xF; vx++) {
            for (int vy = 0; vy <= 0xF; vy++) {
                for (byte n = 0; n <= 0xF; n++) {
                    ctx.vm().setRegister(vx, coordX);
                    ctx.vm().setRegister(vy, coordY);

                    Mockito.reset(ctx.hal());

                    ctx.executor().execute(new OpDXYN(vx, vy, n));

                    if (n == 0) {
                        verifyNoInteractions(ctx.hal());
                        continue;
                    }

                    ArgumentCaptor<Byte> drawXCaptor = ArgumentCaptor.forClass(Byte.class);
                    ArgumentCaptor<Byte> drawYCaptor = ArgumentCaptor.forClass(Byte.class);
                    ArgumentCaptor<Boolean> drawNCaptor = ArgumentCaptor.forClass(Boolean.class);

                    verify(ctx.hal(), times(n * 8))
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

        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_EX9E() {
        ExecutorTestContext ctx = createExecutorContext();

        int pc = ctx.vm().getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            ctx.vm().setPC(pc);

            ctx.executor().execute(new OpEX9E(vx));
            assertEquals(pc, ctx.vm().getPC(), String.format("vx: %X", vx));

            for (byte key = 0; key <= 0xF; key++) {
                ctx.vm().setPC(pc);
                when(ctx.hal().getKey()).thenReturn(Optional.of(key));

                ctx.executor().execute(new OpEX9E(vx));
                assertEquals(key == magic ? pc + 2 : pc, ctx.vm().getPC(),
                    String.format("vx: %X key :%X", vx, key));
            }
        }

        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_EXA1() {
        ExecutorTestContext ctx = createExecutorContext();

        int pc = ctx.vm().getOrigin();
        byte magic = 0xA;
        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setRegister(vx, magic);
            ctx.vm().setPC(pc);

            ctx.executor().execute(new OpEXA1(vx));
            assertEquals(pc + 2, ctx.vm().getPC(), String.format("vx: %X", vx));

            for (byte key = 0; key <= 0xF; key++) {
                ctx.vm().setPC(pc);
                when(ctx.hal().getKey()).thenReturn(Optional.of(key));

                ctx.executor().execute(new OpEXA1(vx));
                assertEquals(key != magic ? pc + 2 : pc, ctx.vm().getPC(),
                    String.format("vx: %X key :%X", vx, key));
            }
        }

        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_FX07() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.vm().setDelayTimer((byte) imm);
                ctx.executor().execute(new OpFX07(vx));

                assertEquals(imm, ctx.vm().getRegister(vx),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_FX0A() {
        ExecutorTestContext ctx = createExecutorContext();
        int pc = ctx.vm().getOrigin();

        for (int vx = 0; vx <= 0xF; vx++) {
            ctx.vm().setPC(pc);
            when(ctx.hal().getKey()).thenReturn(Optional.empty());

            ctx.executor().execute(new OpFX0A(vx));

            assertEquals(pc - 2, ctx.vm().getPC(), String.format("vx: %X", vx));

            for (byte key = 0; key <= 0xF; key++) {
                ctx.vm().setPC(pc);
                when(ctx.hal().getKey()).thenReturn(Optional.of(key));

                ctx.executor().execute(new OpFX0A(vx));
                assertEquals(pc, ctx.vm().getPC(), String.format("vx: %X, key: %X", vx, key));
                assertEquals(key, ctx.vm().getRegister(vx),
                    String.format("vx: %X, key: %X", vx, key));
            }
        }

        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_FX15() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX15(vx));

                assertEquals(ctx.vm().getDelayTimer(), (byte) imm,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_FX18() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX18(vx));

                assertEquals(ctx.vm().hasSound(), imm != 0,
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test protected void execute_FX1E() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                short I = ctx.vm().getI();
                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX1E(vx));

                assertEquals((short) (I + (0xFF & imm)), ctx.vm().getI(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_FX29() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (byte imm = 0; imm <= 0xF; imm++) {
                when(ctx.cal().getCharacterAddress(anyByte())).then(
                    invocationOnMock -> CharacterSprites.getAddressLocator()
                        .getCharacterAddress(invocationOnMock.getArgument(0)));

                ctx.executor().execute(new Op6XNN(vx, imm));
                ctx.executor().execute(new OpFX29(vx));

                assertEquals(CharacterSprites.getAddressLocator().getCharacterAddress(imm),
                    ctx.vm().getI(), String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
    }

    @Test void execute_FX33() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                short I = ctx.vm().getI();
                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX33(vx));

                String num = padStart(Integer.toString(Byte.toUnsignedInt((byte) imm)), 3, '0');

                String message = String.format("vx: %X, imm: %X", vx, (byte) imm);
                assertEquals(num.charAt(0) - '0', ctx.vm().getByte(I), message);
                assertEquals(num.charAt(1) - '0', ctx.vm().getByte(I + 1), message);
                assertEquals(num.charAt(2) - '0', ctx.vm().getByte(I + 2), message);
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @ParameterizedTest @ValueSource(booleans = {true, false}) void execute_FX55(
        boolean loadDumpIncreasesI) {
        ExecutorTestContext ctx = createExecutorContext(false, loadDumpIncreasesI);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.vm().setI((short) 0x526);
                short I = ctx.vm().getI();

                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX55(vx));

                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : 0xC0 | x * 2 + 1), ctx.vm().getByte(I + x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }

                assertEquals(loadDumpIncreasesI ? I + vx + 1 : I, ctx.vm().getI(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));

            }
            ctx.vm().setRegister(vx, (byte) (0xC0 | (vx * 2 + 1)));
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @ParameterizedTest @ValueSource(booleans = {true, false}) void execute_FX65(
        boolean loadDumpIncreasesI) {
        ExecutorTestContext ctx = createExecutorContext(false, loadDumpIncreasesI);

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                ctx.vm().setI((short) 0x526);

                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX55(vx));
                ctx.executor().execute(new OpFX55(vx));
                for (byte x = 0; x <= vx; x++) {
                    ctx.vm().setRegister(x, (byte) 0x00);
                }

                ctx.vm().setI((short) 0x526);
                short I = ctx.vm().getI();
                ctx.executor().execute(new OpFX65(vx));
                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : 0xB0 | x * 2 + 1), ctx.vm().getRegister(x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }
                assertEquals(loadDumpIncreasesI ? I + vx + 1 : I, ctx.vm().getI(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));

                ctx.executor().execute(new OpFX65(vx));
                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : 0xB0 | x * 2 + 1), ctx.vm().getRegister(x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }
                assertEquals(loadDumpIncreasesI ? I + 2 * (vx + 1) : I, ctx.vm().getI(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
            ctx.vm().setRegister(vx, (byte) (0xB0 | (vx * 2 + 1)));
        }

        verifyNoInteractions(ctx.hal());
    }
}
