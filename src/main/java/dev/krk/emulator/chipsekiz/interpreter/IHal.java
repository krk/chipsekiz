package dev.krk.emulator.chipsekiz.interpreter;

public interface IHal {
    byte getRand();

    void clearScreen();

    void draw(byte x, byte y, boolean value);

    void sound(boolean active);
}
