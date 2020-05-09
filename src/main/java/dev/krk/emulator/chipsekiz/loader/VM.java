package dev.krk.emulator.chipsekiz.loader;

import java.util.Stack;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class VM {
    private final int memorySize;
    private final int origin;
    private final byte[] memory;
    private final byte[] registers;
    private final Stack<Short> stack;
    private static final int StackLimit = 16;

    public VM() {
        this(0x200, 0x1000);
    }

    public VM(int origin, int memorySize) {
        this.origin = origin;
        this.memorySize = memorySize;
        this.memory = new byte[memorySize];
        this.registers = new byte[16];
        this.stack = new Stack();
    }

    public int getOrigin() {
        return origin;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public byte getRegister(int i) {
        checkArgument(i >= 0 && i <= 0xF, "register index out of bounds.");

        return registers[i];
    }

    public void setRegister(int i, byte value) {
        checkArgument(i >= 0 && i <= 0xF, "register index out of bounds.");

        registers[i] = value;
    }

    public boolean getCarryFlag() {
        return registers[0xF] == 1;
    }

    public void setCarryFlag() {
        registers[0xF] = 1;
    }

    public void resetCarryFlag() {
        registers[0xF] = 0;
    }

    public void push(short value) {
        checkState(stack.size() < StackLimit, "VM stack overflow.");

        stack.push(value);
    }

    public short pop() {
        checkState(stack.size() > 0, "VM stack underflow.");

        return stack.pop();
    }

    public byte getByte(int address) {
        checkArgument(address >= 0 && address < memory.length, "address out of bounds.");

        return memory[address];
    }

    public short getShort(int address) {
        checkArgument(address >= 0 && address + 1 < memory.length, "address out of bounds.");

        return (short) (memory[address] << 8 | memory[address + 1]);
    }

    public int getInt(int address) {
        checkArgument(address >= 0 && address + 3 < memory.length, "address out of bounds.");

        return memory[address] << 24 | memory[address + 1] << 16 | memory[address + 2] << 8
            | memory[address + 3];
    }

    public void load(byte[] program, Layout layout) {
        checkArgument(program.length > 0, "empty program is invalid.");
        checkArgument(layout.isValid(getOrigin(), program.length, getMemorySize()),
            "layout is not compatible with the program.");

        for (Section section : layout.getSections()) {
            System.arraycopy(section.data(), 0, memory, section.start(), section.data().length);
        }
        System.arraycopy(program, 0, memory, getOrigin(), program.length);
    }
}
