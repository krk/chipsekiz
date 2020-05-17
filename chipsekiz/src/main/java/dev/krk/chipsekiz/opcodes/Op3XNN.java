package dev.krk.chipsekiz.opcodes;


/**
 * Skips the next instruction if VX equals NN.
 */
public class Op3XNN extends OpPXNN {
    public Op3XNN(int vx, byte imm) {
        super(3, vx, imm);
    }

    @Override public String toString() {
        return String.format("SE V%X, 0x%02X", vx(), imm());
    }
}
