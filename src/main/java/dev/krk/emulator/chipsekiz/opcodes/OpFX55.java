package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Stores V0 to VX (including VX) in memory starting at address I. The offset from I is increased
 * by 1 for each value written, but I itself is left unmodified.[d]
 */
public class OpFX55 extends OpPXQR {
    public OpFX55(int vx) {
        super(0xF, vx, 5, 5);
    }
}
