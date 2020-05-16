package dev.krk.chipsekiz.opcodes;

/**
 * Calls subroutine at NNN.
 */
public class Op2NNN extends OpPNNN {
    public Op2NNN(short address) {
        super(2, address);
    }
}
