package dev.krk.chipsekiz.opcodes;


/**
 * Sets VX to NN.
 */
public class Op6XNN extends OpPXNN {
    public Op6XNN(int vx, byte imm) {
        super(6, vx, imm);
    }

    @Override public String toString() {
        return String.format("LD V%X, 0x%02X", vx(), imm());
    }
}
