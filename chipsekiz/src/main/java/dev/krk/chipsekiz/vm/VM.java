package dev.krk.chipsekiz.vm;

import java.util.Stack;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


public class VM implements IVirtualMachine {
    private final int origin;
    private final byte[] memory;
    private final byte[] registers;
    private final Stack<Integer> stack;
    private static final int StackLimit = 16;
    private short regI;
    private int regPC;

    private byte timerDelay;
    private byte timerSound;

    public VM() {
        this(0x200, new byte[0x1000]);
    }

    public VM(int origin, byte[] memory) {
        checkArgument(memory.length > 0, "memory cannot be of zero length");
        checkArgument(origin >= 0 && origin < memory.length, "origin must be inside the memory");

        this.origin = origin;
        this.memory = memory;
        this.registers = new byte[16];
        this.stack = new Stack<>();
        setPC(origin);
    }

    @Override public int getPC() {
        return regPC;
    }

    @Override public void setPC(int pc) {
        checkArgument(pc >= 0 && pc <= getMemorySize() + 2, "PC out of bounds.");

        regPC = pc;
    }

    @Override public short getI() {
        return regI;
    }

    @Override public void setI(short i) {
        regI = i;
    }

    @Override public int getOrigin() {
        return origin;
    }

    @Override public int getMemorySize() {
        return memory.length;
    }

    @Override public byte getRegister(int i) {
        checkArgument(i >= 0 && i <= 0xF, "register index out of bounds.");

        return registers[i];
    }

    @Override public void setRegister(int i, byte value) {
        checkArgument(i >= 0 && i <= 0xF, "register index out of bounds.");

        registers[i] = value;
    }

    @Override public boolean hasCarry() {
        return registers[0xF] == 1;
    }

    @Override public void setCarry(boolean carry) {
        registers[0xF] = (byte) (carry ? 1 : 0);
    }

    public void setCarry() {
        setCarry(true);
    }

    @Override public void push(int value) {
        checkState(stack.size() < StackLimit, "VM stack overflow.");

        stack.push(value);
    }

    @Override public int pop() {
        checkState(stack.size() > 0, "VM stack underflow.");

        return stack.pop();
    }

    @Override public void tickTimers() {
        if (timerDelay != 0) {
            timerDelay--;
        }
        if (timerSound != 0) {
            timerSound--;
        }
    }

    public boolean hasDelay() {
        return timerDelay > 0;
    }

    public boolean hasSound() {
        return timerSound != 0;
    }

    @Override public byte getDelayTimer() {
        return timerDelay;
    }

    @Override public void setDelayTimer(byte value) {
        timerDelay = value;
    }

    @Override public byte getSoundTimer() {
        return timerSound;
    }

    @Override public void setSoundTimer(byte value) {
        timerSound = value;
    }

    @Override public void setByte(int address, byte value) {
        checkArgument(address >= 0 && address < memory.length, "address out of bounds.");

        memory[address] = value;
    }

    @Override public byte getByte(int address) {
        checkArgument(address >= 0 && address < memory.length, "address out of bounds.");

        return memory[address];
    }

    public short getShort(int address) {
        checkArgument(address >= 0 && address + 1 < memory.length, "address out of bounds.");

        return (short) (getByte(address) << 8 | (0xFF & getByte(address + 1)));
    }

    public int getInt(int address) {
        checkArgument(address >= 0 && address + 3 < memory.length, "address out of bounds.");

        return (0xFF & getByte(address)) << 24 | (0xFF & getByte(address + 1)) << 16
            | (0xFF & getByte(address + 2)) << 8 | (0xFF & getByte(address + 3));
    }
}
