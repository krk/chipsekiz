package dev.krk.chipsekiz.opcodes;

/**
 * Sets VX to VY minus VX. VF is set to 0 when there's a borrow, and 1 when there isn't.
 */
public class Op8XY7 extends OpPXYQ {
    public Op8XY7(int vx, int vy) {
        super(8, vx, vy, 7);
    }
}
