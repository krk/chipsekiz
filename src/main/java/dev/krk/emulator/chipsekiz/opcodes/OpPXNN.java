package dev.krk.emulator.chipsekiz.opcodes;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.padStart;

public abstract class OpPXNN extends Opcode {
    private final char p;

    public OpPXNN(char p, int vx, int imm) {
        super(Optional.of(vx), Optional.empty(), Optional.of(imm));
        checkArgument(imm == (imm & 0xFF), "immediate out of bounds");
        checkArgument(vx >= 0 && vx <= 0xF, "register index out of bounds");

        this.p = p;
    }

    public int vx() {
        return super.getVx().get();
    }

    public byte imm() {
        return super.getAddress().get().byteValue();
    }

    @Override public String toString() {
        return p + Integer.toHexString(vx()).toUpperCase() + padStart(String.format("%02X", imm()),
            2, '0');
    }
}

