package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Sets VX to the result of a bitwise and operation on a random number (Typically: 0 to 255) and NN.
 */
public class OpCXNN extends OpPXNN {
    public OpCXNN(int vx, byte imm) {
        super(0xC, vx, imm);
    }
}
