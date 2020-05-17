package dev.krk.chipsekiz.opcodes;

/**
 * Skips the next instruction if VX equals VY.
 */
public class Op5XY0 extends OpPXYQ {
    public Op5XY0(int vx, int vy) {
        super(5, vx, vy, 0);
    }

    @Override public String toString() {
        return String.format("SE V%X, V%X", vx(), vy());
    }
}
