package dev.krk.chipsekiz.opcodes;

/**
 * Sets VX to VX xor VY.
 */
public class Op8XY3 extends OpPXYQ {
    public Op8XY3(int vx, int vy) {
        super(8, vx, vy, 3);
    }

    @Override public String toString() {
        return String.format("XOR V%X, V%X", vx(), vy());
    }
}
