package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Skips the next instruction if VX equals NN.
 */
public class Op3XNN extends OpPXNN {
    public Op3XNN(int vx, byte imm) {
        super(3, vx, imm);
    }
}
