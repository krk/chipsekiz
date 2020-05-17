package dev.krk.chipsekiz.opcodes;

/**
 * Stores the least significant bit of VY in VF and then shifts VY to the right by 1 and assigns
 * the result to VX.
 */
public class Op8XY6 extends OpPXYQ {
    public Op8XY6(int vx, int vy) {
        super(8, vx, vy, 6);
    }

    @Override public String toString() {
        return String.format("SHR V%X, V%X", vx(), vy());
    }
}
