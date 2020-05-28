package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.OpPXQR;

/**
 * Restore v0-vX from flag registers.
 */
public class OpFX85 extends OpPXQR {
    public OpFX85(int vx) {
        super(0xF, vx, 8, 5);
    }

    @Override public String toString() {
        return String.format("LD V%X, R", vx());
    }
}
