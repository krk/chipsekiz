package dev.krk.chipsekiz.loader;

public class Loader implements ILoader {
    public static final int DefaultOrigin = 0x200;
    public static final int DefaultMemorySize = 0x1000;

    public byte[] load(byte[] program, Layout layout) {
        return load(DefaultOrigin, program, DefaultMemorySize, layout);
    }

    @Override public byte[] load(int origin, byte[] program, int memorySize, Layout layout) {
        if (program.length == 0) throw new IllegalArgumentException("empty program is invalid.");
        if (!layout.isValid(origin, program.length, memorySize))
            throw new IllegalArgumentException("layout is not compatible with the program.");

        byte[] memory = new byte[memorySize];
        for (Section section : layout.getSections()) {
            System.arraycopy(section.data(), 0, memory, section.start(), section.data().length);
        }
        System.arraycopy(program, 0, memory, origin, program.length);
        return memory;
    }
}
