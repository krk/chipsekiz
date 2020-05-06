package dev.krk.emulator.chipsekiz.opcodes;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public class Op7XNN extends Opcode {
    public Op7XNN(int vx, int imm) {
        super(Optional.of(vx), Optional.empty(), Optional.of(imm));
        checkArgument(imm == (imm & 0xFF), "immediate out of bounds");
        checkArgument(vx >= 0 && vx <= 0xF, "register index out of bounds");
    }

    public int vx() {
        return super.getVx().get();
    }

    public byte imm() {
        return super.getAddress().get().byteValue();
    }

    @Override public String toString() {
        return "7" + Integer.toHexString(vx()) + padStart(Integer.toHexString(imm()).toUpperCase(),
            2, '0');
    }
}

