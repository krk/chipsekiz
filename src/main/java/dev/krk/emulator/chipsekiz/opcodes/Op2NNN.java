package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Calls subroutine at NNN.
 */
public class Op2NNN extends OpPNNN {
    public Op2NNN(int address) {
        super(2, address);
    }
}
