package dev.krk.chipsekiz.opcodes;

/**
 * VX is subtracted from VY and the result is put in VX. VF is set to 0 when there's a borrow,
 * and 1 when there isn't.
 */
public class Op8XYE extends OpPXYQ {
    public Op8XYE(int vx, int vy) {
        super(8, vx, vy, 0xE);
    }

    @Override public String toString() {
        return String.format("SHL V%X, V%X", vx(), vy());
    }
}
