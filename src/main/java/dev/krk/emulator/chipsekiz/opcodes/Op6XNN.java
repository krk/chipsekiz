package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Sets VX to NN.
 */
public class Op6XNN extends OpPXNN {
    public Op6XNN(int vx, int imm) {
        super(6, vx, imm);
    }
}
