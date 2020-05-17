package dev.krk.chipsekiz.opcodes;

/**
 * Skips the next instruction if the key stored in VX is pressed.
 */
public class OpEX9E extends OpPXQR {
    public OpEX9E(int vx) {
        super(0xE, vx, 9, 0xE);
    }

    @Override public String toString() {
        return String.format("SKP V%X", vx());
    }
}

