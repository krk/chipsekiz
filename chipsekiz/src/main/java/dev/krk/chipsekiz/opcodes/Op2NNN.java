package dev.krk.chipsekiz.opcodes;

/**
 * Calls subroutine at NNN.
 */
public class Op2NNN extends OpPNNN {
    public Op2NNN(short address) {
        super(2, address);
    }

    @Override public String toString() {
        return String.format("CALL 0x%03X", address());
    }
}
