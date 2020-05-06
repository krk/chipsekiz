package dev.krk.emulator.chipsekiz.opcodes;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class OpPXQR extends Opcode {
    private final char p;
    private final char q;
    private final char r;

    public OpPXQR(char p, int vx, char q, char r) {
        super(Optional.of(vx), Optional.empty(), Optional.empty());
        checkArgument(vx >= 0 && vx <= 0xF, "register index out of bounds");
        this.p = p;
        this.q = q;
        this.r = r;
    }

    public int vx() {
        return super.getVx().get();
    }

    @Override public String toString() {
        return p + Integer.toHexString(vx()).toUpperCase() + q + r;
    }
}
