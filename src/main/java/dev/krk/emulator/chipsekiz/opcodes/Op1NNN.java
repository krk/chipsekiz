package dev.krk.emulator.chipsekiz.opcodes;


/**
 * Jumps to address NNN.
 */
public class Op1NNN extends OpPNNN {
    public Op1NNN(int address) {
        super(1, address);
    }
}
