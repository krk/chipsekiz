package dev.krk.chipsekiz.opcodes;

/**
 * Sets VX to the value of VY.
 */
public class Op8XY0 extends OpPXYQ {
    public Op8XY0(int vx, int vy) {
        super(8, vx, vy, 0);
    }

    @Override public String toString() {
        return String.format("LD V%X, V%X", vx(), vy());
    }
}
