package dev.krk.chipsekiz.opcodes;


/**
 * Skips the next instruction if VX doesn't equal NN.
 */
public class Op4XNN extends OpPXNN {
    public Op4XNN(int vx, byte imm) {
        super(4, vx, imm);
    }
}
