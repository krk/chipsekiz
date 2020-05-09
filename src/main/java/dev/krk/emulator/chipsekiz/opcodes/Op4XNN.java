package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Skips the next instruction if VX doesn't equal NN.
 */
public class Op4XNN extends OpPXNN {
    public Op4XNN(int vx, int imm) {
        super(4, vx, imm);
    }
}
