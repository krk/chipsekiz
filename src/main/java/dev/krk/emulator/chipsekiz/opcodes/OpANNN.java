package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Sets I to the address NNN.
 */
public class OpANNN extends OpPNNN {
    public OpANNN(short address) {
        super(0xA, address);
    }
}
