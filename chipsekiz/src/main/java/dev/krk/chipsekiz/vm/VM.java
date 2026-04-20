package dev.krk.chipsekiz.vm;

import java.util.Stack;


public class VM implements IVirtualMachine {
    private final int origin;
    private final byte[] memory;
    private final byte[] registers;
    private final Stack<Integer> stack;
    private static final int StackLimit = 16;
    private final IVMDebugger debugger;
    private short regI;
    private int regPC;

    private byte timerDelay;
    private byte timerSound;

    public VM() {
        this(0x200, new byte[0x1000], null);
    }

    public VM(int origin, byte[] memory) {
        this(origin, memory, null);
    }

    public VM(int origin, byte[] memory, IVMDebugger debugger) {
        if (memory.length == 0) throw new IllegalArgumentException("memory cannot be of zero length");
        if (origin < 0 || origin >= memory.length) throw new IllegalArgumentException("origin must be inside the memory");

        this.debugger = debugger;
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
        if (pc < 0 || pc > getMemorySize() + 2) throw new IllegalArgumentException("PC out of bounds.");

        regPC = pc;

        if (debugger != null) {
            debugger.updatePC(pc);
        }
    }

    @Override public short getI() {
        return regI;
    }

    @Override public void setI(short i) {
        regI = i;

        if (debugger != null) {
            debugger.updateI(i);
        }
    }

    @Override public int getOrigin() {
        return origin;
    }

    @Override public int getMemorySize() {
        return memory.length;
    }

    @Override public byte getRegister(int i) {
        if (i < 0 || i > 0xF) throw new IllegalArgumentException("register index out of bounds.");

        return registers[i];
    }

    @Override public void setRegister(int i, byte value) {
        if (i < 0 || i > 0xF) throw new IllegalArgumentException("register index out of bounds.");

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
        if (stack.size() >= StackLimit) throw new IllegalStateException("VM stack overflow: " + Integer.toHexString(value) + ".");

        stack.push(value);
    }

    @Override public int pop() {
        if (stack.isEmpty()) throw new IllegalStateException("VM stack underflow.");

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
        if (address < 0 || address >= memory.length) throw new IllegalArgumentException("address out of bounds.");

        memory[address] = value;

        if (debugger != null) {
            debugger.updatedByte(address);
        }
    }

    @Override public byte getByte(int address) {
        if (address < 0 || address >= memory.length) throw new IllegalArgumentException("address out of bounds.");

        return memory[address];
    }

    public short getShort(int address) {
        if (address < 0 || address + 1 >= memory.length) throw new IllegalArgumentException("address out of bounds.");

        return (short) (getByte(address) << 8 | (0xFF & getByte(address + 1)));
    }

    public int getInt(int address) {
        if (address < 0 || address + 3 >= memory.length) throw new IllegalArgumentException("address out of bounds.");

        return (0xFF & getByte(address)) << 24 | (0xFF & getByte(address + 1)) << 16
            | (0xFF & getByte(address + 2)) << 8 | (0xFF & getByte(address + 3));
    }
}
