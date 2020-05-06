package dev.krk.emulator.chipsekiz.opcodes;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class OpPXYQ extends Opcode {
    private final char p;
    private final char q;

    public OpPXYQ(char p, int vx, int vy, char q) {
        super(Optional.of(vx), Optional.of(vy), Optional.empty());
        checkArgument(vx >= 0 && vx <= 0xF, "register index out of bounds");
        checkArgument(vy >= 0 && vy <= 0xF, "register index out of bounds");
        this.p = p;
        this.q = q;
    }

    public int vx() {
        return super.getVx().get();
    }

    public int vy() {
        return super.getVy().get();
    }

    @Override public String toString() {
        return p + Integer.toHexString(vx()) + Integer.toHexString(vy()) + q;
    }
}
