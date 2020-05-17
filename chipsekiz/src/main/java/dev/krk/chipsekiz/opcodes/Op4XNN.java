package dev.krk.chipsekiz.opcodes;


/**
 * Skips the next instruction if VX doesn't equal NN.
 */
public class Op4XNN extends OpPXNN {
    public Op4XNN(int vx, byte imm) {
        super(4, vx, imm);
    }

    @Override public String toString() {
        return String.format("SNE V%X, 0x%02X", vx(), imm());
    }
}
