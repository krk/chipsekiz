package dev.krk.chipsekiz.opcodes;

/**
 * Sets VX to VX or VY.
 */
public class Op8XY1 extends OpPXYQ {
    public Op8XY1(int vx, int vy) {
        super(8, vx, vy, 1);
    }
}
