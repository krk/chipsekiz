package dev.krk.emulator.chipsekiz.loader;

import static com.google.common.base.Preconditions.checkArgument;

class VM {
    private final int memorySize;
    private final int origin;
    private final byte[] memory;

    public VM() {
        this(0x200, 0x1000);
    }

    public VM(int origin, int memorySize) {
        this.origin = origin;
        this.memorySize = memorySize;
        this.memory = new byte[memorySize];
    }

    public int getOrigin() {
        return origin;
    }

    public int getMemorySize() {
        return memorySize;
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
