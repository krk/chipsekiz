package dev.krk.chipsekiz.opcodes;

/**
 * Adds VY to VX. VF is set to 1 when there's a carry, and to 0 when there isn't.
 */
public class Op8XY4 extends OpPXYQ {
    public Op8XY4(int vx, int vy) {
        super(8, vx, vy, 4);
    }

    @Override public String toString() {
        return String.format("ADD V%X, V%X", vx(), vy());
    }
}
