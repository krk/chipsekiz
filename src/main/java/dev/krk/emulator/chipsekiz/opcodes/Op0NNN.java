package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Calls RCA 1802 program at address NNN. Not necessary for most ROMs.
 * Implemented as 2NNN.
 */
public class Op0NNN extends OpPNNN {
    public Op0NNN(int address) {
        super(0, address);
    }
}
