package dev.krk.chipsekiz.opcodes;

/**
 * VY is subtracted from VX. VF is set to 0 when there's a borrow, and 1 when there isn't.
 */
public class Op8XY5 extends OpPXYQ {
    public Op8XY5(int vx, int vy) {
        super(8, vx, vy, 5);
    }

    @Override public String toString() {
        return String.format("SUB V%X, V%X", vx(), vy());
    }
}
