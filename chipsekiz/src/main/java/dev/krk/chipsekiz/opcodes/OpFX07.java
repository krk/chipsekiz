package dev.krk.chipsekiz.opcodes;

/**
 * Sets VX to the value of the delay timer.
 */
public class OpFX07 extends OpPXQR {
    public OpFX07(int vx) {
        super(0xF, vx, 0, 7);
    }

    @Override public String toString() {
        return String.format("LD V%X, DT", vx());
    }
}
