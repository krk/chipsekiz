package dev.krk.chipsekiz.opcodes;

/**
 * Fills V0 to VX (including VX) with values from memory starting at address I. The offset from I
 * is increased by 1 for each value written, but I itself is left unmodified.
 */
public class OpFX65 extends OpPXQR {
    public OpFX65(int vx) {
        super(0xF, vx, 6, 5);
    }

    @Override public String toString() {
        return String.format("LD V%X, [I]", vx());
    }
}
