package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Sets VX to NN.
 */
public class Op6XNN extends OpPXNN {
    public Op6XNN(int vx, byte imm) {
        super(6, vx, imm);
    }
}
