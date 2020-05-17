package dev.krk.chipsekiz.opcodes;

/**
 * Sets VX to VX and VY.
 */
public class Op8XY2 extends OpPXYQ {
    public Op8XY2(int vx, int vy) {
        super(8, vx, vy, 2);
    }

    @Override public String toString() {
        return String.format("AND V%X, V%X", vx(), vy());
    }
}
