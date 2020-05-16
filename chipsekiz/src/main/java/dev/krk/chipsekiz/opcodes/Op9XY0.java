package dev.krk.chipsekiz.opcodes;

/**
 * Skips the next instruction if VX doesn't equal VY.
 */
public class Op9XY0 extends OpPXYQ {
    public Op9XY0(int vx, int vy) {
        super(9, vx, vy, 0);
    }
}
