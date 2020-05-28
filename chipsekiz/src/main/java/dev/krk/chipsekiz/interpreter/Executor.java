package dev.krk.chipsekiz.interpreter;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
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
import dev.krk.chipsekiz.opcodes.Opcode;
import dev.krk.chipsekiz.vm.IVirtualMachine;

import javax.annotation.Nullable;

import java.util.Optional;

public class Executor implements IExecutor {
    private IVirtualMachine vm;
    private final IHal hal;
    private final ICharacterAddressLocator characterAddressLocator;
    private final boolean bitShiftsIgnoreVY;
    private final boolean saveDumpIncreasesI;

    public Executor(IHal hal, ICharacterAddressLocator characterAddressLocator) {
        this(null, hal, characterAddressLocator, false, false);
    }

    public Executor(@Nullable IVirtualMachine vm, IHal hal,
        ICharacterAddressLocator characterAddressLocator) {
        this(vm, hal, characterAddressLocator, false, false);
    }

    /**
     * @param bitShiftsIgnoreVY Ignore vy in 8XY6 and 8XYE bit-shift operations.
     * https://en.wikipedia.org/wiki/CHIP-8#cite_note-rcaops-13
     * @param loadDumpIncreasesI FX55 and FX65 register load-dump operations increase I.
     * https://en.wikipedia.org/wiki/CHIP-8#cite_note-increment-16
     */
    public Executor(@Nullable IVirtualMachine vm, IHal hal,
        ICharacterAddressLocator characterAddressLocator, boolean bitShiftsIgnoreVY,
        boolean loadDumpIncreasesI) {
        this.vm = vm;
        this.hal = hal;
        this.characterAddressLocator = characterAddressLocator;
        this.bitShiftsIgnoreVY = bitShiftsIgnoreVY;
        this.saveDumpIncreasesI = loadDumpIncreasesI;
    }

    @Override public void setVM(IVirtualMachine vm) {
        this.vm = vm;
    }

    @Override public void execute(Opcode opcode) {
        switch (opcode.getKind()) {
            case Op00E0 -> hal.clearScreen();
            case Op00EE -> vm.setPC(vm.pop());
            case Op0NNN -> {
                // NOP
            }
            case Op1NNN -> vm.setPC(((Op1NNN) opcode).address());
            case Op2NNN -> {
                vm.push(vm.getPC());
                vm.setPC(((Op2NNN) opcode).address());
            }
            case Op3XNN -> {
                Op3XNN o = (Op3XNN) opcode;
                if (vm.getRegister(o.vx()) == o.imm()) {
                    vm.setPC(vm.getPC() + 2);
                }
            }
            case Op4XNN -> {
                Op4XNN o = (Op4XNN) opcode;
                if (vm.getRegister(o.vx()) != o.imm()) {
                    vm.setPC(vm.getPC() + 2);
                }
            }
            case Op5XY0 -> {
                Op5XY0 o = (Op5XY0) opcode;
                if (vm.getRegister(o.vx()) == vm.getRegister(o.vy())) {
                    vm.setPC(vm.getPC() + 2);
                }
            }
            case Op6XNN -> {
                Op6XNN o = (Op6XNN) opcode;
                vm.setRegister(o.vx(), o.imm());
            }
            case Op7XNN -> {
                Op7XNN o = (Op7XNN) opcode;
                vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) + o.imm()));
            }
            case Op8XY0 -> {
                Op8XY0 o = (Op8XY0) opcode;
                vm.setRegister(o.vx(), vm.getRegister(o.vy()));
            }
            case Op8XY1 -> {
                Op8XY1 o = (Op8XY1) opcode;
                vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) | vm.getRegister(o.vy())));
            }
            case Op8XY2 -> {
                Op8XY2 o = (Op8XY2) opcode;
                vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) & vm.getRegister(o.vy())));
            }
            case Op8XY3 -> {
                Op8XY3 o = (Op8XY3) opcode;
                vm.setRegister(o.vx(), (byte) (vm.getRegister(o.vx()) ^ vm.getRegister(o.vy())));
            }
            case Op8XY4 -> {
                Op8XY4 o = (Op8XY4) opcode;
                int x = vm.getRegister(o.vx()) & 0xFF;
                int y = vm.getRegister(o.vy()) & 0xFF;
                int sum = x + y;
                vm.setRegister(o.vx(), (byte) (sum & 0xFF));
                vm.setCarry((sum & 0x1FF) != (sum & 0xFF));
            }
            case Op8XY5 -> {
                Op8XY5 o = (Op8XY5) opcode;
                int x = vm.getRegister(o.vx()) & 0xFF;
                int y = vm.getRegister(o.vy()) & 0xFF;
                int diff = x - y;
                vm.setRegister(o.vx(), (byte) (diff & 0xFF));
                vm.setCarry((diff & 0x1FF) == (diff & 0xFF));
            }
            case Op8XY6 -> {
                Op8XY6 o = (Op8XY6) opcode;
                int x = vm.getRegister(bitShiftsIgnoreVY ? o.vx() : o.vy()) & 0xFF;
                vm.setRegister(o.vx(), (byte) (x >> 1));
                vm.setCarry((x & 0x1) == 0x1);
            }
            case Op8XY7 -> {
                Op8XY7 o = (Op8XY7) opcode;
                int x = vm.getRegister(o.vx()) & 0xFF;
                int y = vm.getRegister(o.vy()) & 0xFF;
                int diff = y - x;
                vm.setRegister(o.vx(), (byte) (diff & 0xFF));
                vm.setCarry((diff & 0x1FF) == (diff & 0xFF));
            }
            case Op8XYE -> {
                Op8XYE o = (Op8XYE) opcode;
                int x = vm.getRegister(bitShiftsIgnoreVY ? o.vx() : o.vy()) << 1;
                vm.setRegister(o.vx(), (byte) (x & 0xFF));
                vm.setCarry((x & 0x100) == 0x100);
            }
            case Op9XY0 -> {
                Op9XY0 o = (Op9XY0) opcode;
                if (vm.getRegister(o.vx()) != vm.getRegister(o.vy())) {
                    vm.setPC(vm.getPC() + 2);
                }
            }
            case OpANNN -> vm.setI(((OpANNN) opcode).address());
            case OpBNNN -> vm.setPC(
                (short) (((vm.getRegister(0) & 0xFF) + ((OpBNNN) opcode).address())) & 0xFFFF);
            case OpCXNN -> {
                OpCXNN opCXNN = (OpCXNN) opcode;
                vm.setRegister(opCXNN.vx(), (byte) (opCXNN.imm() & hal.getRand()));
            }
            case OpDXYN -> {
                OpDXYN o = (OpDXYN) opcode;
                boolean flipped = false;
                int I = vm.getI();

                for (byte n = 0; n < o.imm(); n++) {
                    byte row = vm.getByte(I + n);
                    for (byte bit = 0; bit < 8; bit++) {
                        boolean flip = (row & (1 << bit)) == 1 << bit;
                        flipped = flipped | hal.draw((byte) (vm.getRegister(o.vx()) + 7 - bit),
                            (byte) (vm.getRegister(o.vy()) + n), flip);
                    }
                }
                vm.setCarry(flipped);
            }
            case OpEX9E -> {
                Optional<Byte> key = hal.getKey();
                if (key.isPresent() && key.get() == vm.getRegister(((OpEX9E) opcode).vx())) {
                    vm.setPC(vm.getPC() + 2);
                }
            }
            case OpEXA1 -> {
                Optional<Byte> key = hal.getKey();
                if (key.isEmpty() || key.get() != vm.getRegister(((OpEXA1) opcode).vx())) {
                    vm.setPC(vm.getPC() + 2);
                }
            }
            case OpFX07 -> vm.setRegister(((OpFX07) opcode).vx(), vm.getDelayTimer());
            case OpFX0A -> {
                Optional<Byte> key = hal.getKey();
                if (key.isEmpty()) {
                    // Block until a key is pressed.
                    vm.setPC(vm.getPC() - 2);
                    return;
                }
                vm.setRegister(((OpFX0A) opcode).vx(), key.get());
            }
            case OpFX15 -> vm.setDelayTimer(vm.getRegister(((OpFX15) opcode).vx()));
            case OpFX18 -> vm.setSoundTimer(vm.getRegister(((OpFX18) opcode).vx()));
            case OpFX1E -> vm.setI(
                (short) (0xFFFF & vm.getI() + (0xFF & vm.getRegister(((OpFX1E) opcode).vx()))));
            case OpFX29 -> vm.setI(characterAddressLocator
                .getCharacterAddress(vm.getRegister(((OpFX29) opcode).vx())));
            case OpFX33 -> {
                int I = vm.getI();
                int bcd = vm.getRegister(((OpFX33) opcode).vx()) & 0xFF;
                vm.setByte(I, (byte) (bcd / 100));
                vm.setByte(I + 1, (byte) ((bcd % 100) / 10));
                vm.setByte(I + 2, (byte) (bcd % 10));
            }
            case OpFX55 -> {
                OpFX55 opFX55 = (OpFX55) opcode;
                int I = vm.getI();
                for (int x = 0; x <= opFX55.vx(); x++) {
                    vm.setByte(I + x, vm.getRegister(x));
                }
                if (saveDumpIncreasesI) {
                    vm.setI((short) (0xFFFF & (I + opFX55.vx() + 1)));
                }
            }
            case OpFX65 -> {
                OpFX65 opFX65 = (OpFX65) opcode;
                int I = vm.getI();
                for (int x = 0; x <= opFX65.vx(); x++) {
                    vm.setRegister(x, vm.getByte(I + x));
                }
                if (saveDumpIncreasesI) {
                    vm.setI((short) (0xFFFF & (I + opFX65.vx() + 1)));
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + opcode.getKind());
        }
    }
}
