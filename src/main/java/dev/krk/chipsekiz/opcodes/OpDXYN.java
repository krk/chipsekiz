package dev.krk.chipsekiz.opcodes;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Draws a sprite at coordinate (VX, VY) that has a width of 8 pixels and a height of N pixels.
 * Each row of 8 pixels is read as bit-coded starting from memory location I; I value doesn’t
 * change after the execution of this instruction. As described above, VF is set to 1 if any screen
 * pixels are flipped from set to unset when the sprite is drawn, and to 0 if that doesn’t happen
 */
public class OpDXYN extends Opcode {
    public OpDXYN(int vx, int vy, int imm) {
        super(vx, vy, imm);
        checkArgument(vx >= 0 && vx <= 0xF, "register index out of bounds");
        checkArgument(vy >= 0 && vy <= 0xF, "register index out of bounds");
        checkArgument(imm >= 0 && imm <= 0xF, "immediate index out of bounds");
    }

    public int vx() {
        return super.getVx().get();
    }

    public int vy() {
        return super.getVy().get();
    }

    public int imm() {
        return super.getAddress().get();
    }

    @Override public short getValue() {
        return (short) (0xD << 12 | vx() << 8 | vy() << 4 | imm());
    }

    @Override public String encode() {
        return String.format("D%1X%1X%1X", vx(), vy(), imm());
    }
}
