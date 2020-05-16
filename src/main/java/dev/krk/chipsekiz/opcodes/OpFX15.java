package dev.krk.chipsekiz.opcodes;

/**
 * Sets the delay timer to VX.
 */
public class OpFX15 extends OpPXQR {
    public OpFX15(int vx) {
        super(0xF, vx, 1, 5);
    }
}
