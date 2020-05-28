package dev.krk.chipsekiz.superchip.opcodes;

import dev.krk.chipsekiz.opcodes.OpDXYN;


/**
 * Draw a 16x16 sprite.
 */
public class OpDXY0 extends OpDXYN {
    public OpDXY0(int vx, int vy) {
        super(vx, vy, 0);
    }
}

