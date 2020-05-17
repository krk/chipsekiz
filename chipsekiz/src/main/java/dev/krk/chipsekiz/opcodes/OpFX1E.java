package dev.krk.chipsekiz.opcodes;

/**
 * Adds VX to I. VF is set to 1 when there is a range overflow (I+VX>0xFFF), and to 0 when there
 * isn't.
 */
public class OpFX1E extends OpPXQR {
    public OpFX1E(int vx) {
        super(0xF, vx, 1, 0xE);
    }

    @Override public String toString() {
        return String.format("ADD I, V%X", vx());
    }
}
