package dev.krk.emulator.chipsekiz.opcodes;

/**
 * Jumps to the address NNN plus V0.
 */
public class OpBNNN extends OpPNNN {
    public OpBNNN(short address) {
        super(0xB, address);
    }
}
