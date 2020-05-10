package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Skips the next instruction if the key stored in VX isn't pressed.
 */
public class OpEXA1 extends OpPXQR {
    public OpEXA1(int vx) {
        super(0xE, vx, 0xA, 1);
    }
}
