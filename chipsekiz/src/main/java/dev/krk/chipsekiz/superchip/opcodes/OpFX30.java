package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.OpPXQR;

/**
 * Set i to a large hexadecimal character based on the value of vx.
 */
public class OpFX30 extends OpPXQR {
    public OpFX30(int vx) {
        super(0xF, vx, 3, 0);
    }

    @Override public String toString() {
        return String.format("LD HF, V%X", vx());
    }
}
