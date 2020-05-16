package dev.krk.chipsekiz.loader;

public interface ILoader {
    byte[] load(int origin, byte[] program, int memorySize, Layout layout);
}
