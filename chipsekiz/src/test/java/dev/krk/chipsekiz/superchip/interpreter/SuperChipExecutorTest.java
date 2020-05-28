package dev.krk.chipsekiz.superchip.interpreter;

import dev.krk.chipsekiz.interpreter.ExecutorTest;
import dev.krk.chipsekiz.interpreter.ExecutorTestContext;
import dev.krk.chipsekiz.interpreter.IExecutor;
import dev.krk.chipsekiz.opcodes.Op6XNN;
import dev.krk.chipsekiz.opcodes.OpFX1E;
import dev.krk.chipsekiz.superchip.hal.ISuperChipCharacterAddressLocator;
import dev.krk.chipsekiz.superchip.opcodes.Op00CN;
import dev.krk.chipsekiz.superchip.opcodes.Op00FB;
import dev.krk.chipsekiz.superchip.opcodes.Op00FC;
import dev.krk.chipsekiz.superchip.opcodes.Op00FD;
import dev.krk.chipsekiz.superchip.opcodes.Op00FE;
import dev.krk.chipsekiz.superchip.opcodes.Op00FF;
import dev.krk.chipsekiz.superchip.opcodes.OpDXY0;
import dev.krk.chipsekiz.superchip.opcodes.OpFX30;
import dev.krk.chipsekiz.superchip.opcodes.OpFX75;
import dev.krk.chipsekiz.superchip.opcodes.OpFX85;
import dev.krk.chipsekiz.superchip.sprites.SuperChipCharacterSprites;
import dev.krk.chipsekiz.superchip.vm.ISuperChipVirtualMachine;
import dev.krk.chipsekiz.superchip.vm.SuperChipVM;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SuperExecutorTestContext extends ExecutorTestContext {
    private ISuperChipVirtualMachine vm;
    private ISuperChipHal hal;
    private ISuperChipCharacterAddressLocator cal;

    public ISuperChipVirtualMachine vm() {return vm;}

    public ISuperChipHal hal() {return hal;}

    public ISuperChipCharacterAddressLocator cal() {return cal;}

    public SuperExecutorTestContext(SuperChipVM vm, ISuperChipHal hal,
        ISuperChipCharacterAddressLocator cal, IExecutor executor) {
        super(vm, hal, cal, executor);

        this.vm = vm;
        this.hal = hal;
        this.cal = cal;
    }
}


public class SuperChipExecutorTest extends ExecutorTest {
    @Override protected SuperExecutorTestContext createExecutorContext(boolean bitShiftsIgnoreVY,
        boolean loadDumpIncreasesI) {
        SuperChipVM vm = new SuperChipVM();
        ISuperChipHal hal = mock(ISuperChipHal.class);
        ISuperChipCharacterAddressLocator cal = mock(ISuperChipCharacterAddressLocator.class);
        IExecutor executor =
            new SuperChipExecutor(vm, hal, cal, bitShiftsIgnoreVY, loadDumpIncreasesI);

        return new SuperExecutorTestContext(vm, hal, cal, executor);
    }

    @Override protected SuperExecutorTestContext createExecutorContext(int origin, byte[] memory) {
        SuperChipVM vm = new SuperChipVM(origin, memory, null);
        ISuperChipHal hal = mock(ISuperChipHal.class);
        ISuperChipCharacterAddressLocator cal = mock(ISuperChipCharacterAddressLocator.class);
        IExecutor executor = new SuperChipExecutor(vm, hal, cal);

        return new SuperExecutorTestContext(vm, hal, cal, executor);
    }

    @Override protected SuperExecutorTestContext createExecutorContext() {
        SuperChipVM vm = new SuperChipVM();
        ISuperChipHal hal = mock(ISuperChipHal.class);
        ISuperChipCharacterAddressLocator cal = mock(ISuperChipCharacterAddressLocator.class);
        IExecutor executor = new SuperChipExecutor(vm, hal, cal);

        return new SuperExecutorTestContext(vm, hal, cal, executor);
    }

    @Test void execute_00CN() {
        SuperExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00CN((byte) 5));

        verify(ctx.hal()).scrollDown((byte) 5);
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_00FB() {
        SuperExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00FB());

        verify(ctx.hal()).scrollRight();
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_00FC() {
        SuperExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00FC());

        verify(ctx.hal()).scrollLeft();
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_00FD() {
        SuperExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00FD());

        verify(ctx.hal()).exit();
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_00FE() {
        SuperExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00FE());

        verify(ctx.hal()).setResolution(Resolution.Low);
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_00FF() {
        SuperExecutorTestContext ctx = createExecutorContext();

        ctx.executor().execute(new Op00FF());

        verify(ctx.hal()).setResolution(Resolution.High);
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_DXY0() {
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
                ctx.vm().setRegister(vx, coordX);
                ctx.vm().setRegister(vy, coordY);

                Mockito.reset(ctx.hal());

                ctx.executor().execute(new OpDXY0(vx, vy));

                ArgumentCaptor<Byte> drawXCaptor = ArgumentCaptor.forClass(Byte.class);
                ArgumentCaptor<Byte> drawYCaptor = ArgumentCaptor.forClass(Byte.class);
                ArgumentCaptor<Boolean> drawNCaptor = ArgumentCaptor.forClass(Boolean.class);

                verify(ctx.hal(), times(16 * 16))
                    .draw(drawXCaptor.capture(), drawYCaptor.capture(), drawNCaptor.capture());

                List<Byte> xs = drawXCaptor.getAllValues();
                List<Byte> ys = drawYCaptor.getAllValues();

                int i = 0;
                for (byte dx = 0; dx <= 0xF; dx++) {
                    byte row = (byte) (coordY + 0);
                    byte col = vx == vy ? (byte) (coordY + 15 - dx) : (byte) (coordX + 15 - dx);

                    String message = String.format("vx: %d vy: %d dy: %d dx: %d", vx, vy, 0, dx);

                    assertEquals(col, xs.get(i), message);
                    assertEquals(row, ys.get(i), message);
                    i++;
                }
            }
        }

        verifyNoInteractions(ctx.cal());
    }

    @Override @Test protected void execute_FX1E() {
        ExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                short I = ctx.vm().getI();
                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX1E(vx));

                assertEquals((short) (0xFFF & (I + (0xFF & imm))), ctx.vm().getI(),
                    String.format("vx: %X, imm: %X", vx, (byte) imm));
            }
        }

        verifyNoInteractions(ctx.hal());
        verifyNoInteractions(ctx.cal());
    }

    @Test void execute_FX30() {
        SuperExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (byte imm = 0; imm <= 0xF; imm++) {
                when(ctx.cal().getLargeCharacterAddress(anyByte())).then(
                    invocationOnMock -> SuperChipCharacterSprites.getLargeAddressLocator()
                        .getCharacterAddress(invocationOnMock.getArgument(0)));

                ctx.executor().execute(new Op6XNN(vx, imm));
                ctx.executor().execute(new OpFX30(vx));

                assertEquals(
                    SuperChipCharacterSprites.getLargeAddressLocator().getCharacterAddress(imm),
                    ctx.vm().getI(), String.format("vx: %X, imm: %X", vx, imm));
            }
        }

        verifyNoInteractions(ctx.hal());
    }

    @Test void execute_FX75_FX85() {
        SuperExecutorTestContext ctx = createExecutorContext();

        for (int vx = 0; vx <= 0xF; vx++) {
            for (int imm = Byte.MIN_VALUE; imm <= Byte.MAX_VALUE; imm++) {
                for (byte x = 0; x <= vx; x++) {
                    ctx.vm().setRegister(x, (byte) (imm + 99 + x));
                }

                ctx.executor().execute(new Op6XNN(vx, (byte) imm));
                ctx.executor().execute(new OpFX75(vx));
                for (byte x = 0; x <= 0xF; x++) {
                    if (x == vx) {
                        continue;
                    }
                    ctx.vm().setRegister(x, (byte) 0);
                }
                ctx.executor().execute(new OpFX85(vx));

                for (byte x = 0; x <= vx; x++) {
                    assertEquals((byte) (x == vx ? imm : (byte) (imm + 99 + x)),
                        ctx.vm().getRegister(x),
                        String.format("vx: %X, imm: %X, x: %X", vx, (byte) imm, x));
                }
            }
            ctx.vm().setRegister(vx, (byte) (0xB0 | (vx * 2 + 1)));
        }

        verifyNoInteractions(ctx.hal());
    }
}
