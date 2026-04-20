package dev.krk.chipsekiz.superchip.interpreter;

import dev.krk.chipsekiz.interpreter.Executor;
import dev.krk.chipsekiz.interpreter.IExecutor;
import dev.krk.chipsekiz.opcodes.OpFX1E;
import dev.krk.chipsekiz.opcodes.Opcode;
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
import dev.krk.chipsekiz.superchip.vm.ISuperChipVirtualMachine;
import dev.krk.chipsekiz.vm.IVirtualMachine;

public class SuperChipExecutor extends Executor implements IExecutor {
    private ISuperChipVirtualMachine vm;
    private final ISuperChipHal hal;
    private final ISuperChipCharacterAddressLocator characterAddressLocator;

    public SuperChipExecutor(ISuperChipVirtualMachine vm, ISuperChipHal hal,
        ISuperChipCharacterAddressLocator characterAddressLocator, boolean bitShiftsIgnoreVY,
        boolean loadDumpIncreasesI) {
        super(vm, hal, characterAddressLocator, bitShiftsIgnoreVY, loadDumpIncreasesI);
        this.vm = vm;
        this.hal = hal;
        this.characterAddressLocator = characterAddressLocator;
    }

    public SuperChipExecutor(ISuperChipVirtualMachine vm, ISuperChipHal hal,
        ISuperChipCharacterAddressLocator characterAddressLocator) {
        this(vm, hal, characterAddressLocator, true, false);
    }

    @Override public void setVM(IVirtualMachine vm) {
        super.setVM(vm);

        if (vm instanceof ISuperChipVirtualMachine svm) {
            this.vm = svm;
        }
    }

    @Override public void execute(Opcode opcode) {
        switch (opcode) {
            case Op00CN o -> hal.scrollDown(o.imm());
            case Op00FB _ -> hal.scrollRight();
            case Op00FC _ -> hal.scrollLeft();
            case Op00FD _ -> hal.exit();
            case Op00FE _ -> hal.setResolution(Resolution.Low);
            case Op00FF _ -> hal.setResolution(Resolution.High);
            case OpDXY0 o -> {
                boolean flipped = false;
                int I = vm.getI();

                for (byte n = 0; n < 16; n++) {
                    byte row = vm.getByte(I + n);
                    for (byte bit = 0; bit < 16; bit++) {
                        boolean flip = (row & (1 << bit)) == 1 << bit;
                        flipped = flipped | hal.draw((byte) (vm.getRegister(o.vx()) + 15 - bit),
                            (byte) (vm.getRegister(o.vy()) + n), flip);
                    }
                }
                vm.setCarry(flipped);
            }
            case OpFX1E o -> {
                // Undocumented: Check FX1E (I = I + VX) buffer overflow. If buffer overflow,
                // register VF must be set to 1, otherwise 0.
                int i = (0xFFFF & vm.getI() + (0xFF & vm.getRegister(o.vx())));
                vm.setCarry(i > 0xFFF);
                vm.setI((short) (i & 0xFFF));
            }
            case OpFX30 o -> vm.setI(characterAddressLocator
                .getLargeCharacterAddress(vm.getRegister(o.vx())));
            case OpFX75 o -> {
                byte[] values = new byte[o.vx() + 1];
                for (int x = 0; x <= o.vx(); x++) {
                    values[x] = vm.getRegister(x);
                }

                vm.swapRegisterBank();
                for (int x = 0; x <= o.vx(); x++) {
                    vm.setRegister(x, values[x]);
                }
                vm.swapRegisterBank();
            }
            case OpFX85 o -> {
                vm.swapRegisterBank();
                byte[] values = new byte[o.vx() + 1];
                for (int x = 0; x <= o.vx(); x++) {
                    values[x] = vm.getRegister(x);
                }
                vm.swapRegisterBank();

                for (int x = 0; x <= o.vx(); x++) {
                    vm.setRegister(x, values[x]);
                }
            }

            default -> super.execute(opcode);
        }
    }
}
