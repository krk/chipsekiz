package dev.krk.chipsekiz.opcodes;

/**
 * Sets the sound timer to VX.
 */
public class OpFX18 extends OpPXQR {
    public OpFX18(int vx) {
        super(0xF, vx, 1, 8);
    }
}
