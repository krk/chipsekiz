package dev.krk.emulator.chipsekiz.loader;

public interface ILoader {
    byte[] load(int origin, byte[] program, int memorySize, Layout layout);
}
