package dev.krk.chipsekiz.opcodes;


/**
 * Sets VX to the result of a bitwise and operation on a random number (Typically: 0 to 255) and NN.
 */
public class OpCXNN extends OpPXNN {
    public OpCXNN(int vx, byte imm) {
        super(0xC, vx, imm);
    }

    @Override public String toString() {
        return String.format("RND V%X, 0x%02X", vx(), imm());
    }
}
