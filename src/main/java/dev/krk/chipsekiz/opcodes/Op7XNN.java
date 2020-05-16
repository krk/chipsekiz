package dev.krk.chipsekiz.opcodes;


/**
 * Adds NN to VX. (Carry flag is not changed).
 */
public class Op7XNN extends OpPXNN {
    public Op7XNN(int vx, byte imm) {
        super(7, vx, imm);
    }
}
