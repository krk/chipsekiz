package dev.krk.chipsekiz.opcodes;


/**
 * Jumps to address NNN.
 */
public class Op1NNN extends OpPNNN {
    public Op1NNN(short address) {
        super(1, address);
    }

    @Override public String toString() {
        return String.format("JP 0x%03X", address());
    }
}
