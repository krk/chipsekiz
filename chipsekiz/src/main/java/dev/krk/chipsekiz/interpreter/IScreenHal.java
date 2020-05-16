package dev.krk.chipsekiz.interpreter;

public interface IScreenHal {
    void clearScreen();

    boolean draw(byte x, byte y, boolean value);
}
