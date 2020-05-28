package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.OpPXQR;

/**
 * Save v0-vX to flag registers.
 */
public class OpFX75 extends OpPXQR {
    public OpFX75(int vx) {
        super(0xF, vx, 7, 5);
    }

    @Override public String toString() {
        return String.format("LD R, V%X", vx());
    }
}
