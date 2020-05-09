package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Sets VX to VX xor VY.
 */
public class Op8XY3 extends OpPXYQ {
    public Op8XY3(int vx, int vy) {
        super(8, vx, vy, 3);
    }
}
