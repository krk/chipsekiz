package dev.krk.emulator.chipsekiz.opcodes;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class OpDXYN extends Opcode {
    public OpDXYN(int vx, int vy, int imm) {
        super(Optional.of(vx), Optional.of(vy), Optional.of(imm));
        checkArgument(vx >= 0 && vx <= 0xF, "register index out of bounds");
        checkArgument(vy >= 0 && vy <= 0xF, "register index out of bounds");
        checkArgument(imm >= 0 && imm <= 0xF, "immediate index out of bounds");
    }

    public int vx() {
        return super.getVx().get();
    }

    public int vy() {
        return super.getVy().get();
    }

    public int imm() {
        return super.getAddress().get();
    }

    @Override public String toString() {
        return "D" + Integer.toHexString(vx()).toUpperCase() + Integer.toHexString(vy())
            .toUpperCase() + Integer.toHexString(imm()).toUpperCase();
    }
}
