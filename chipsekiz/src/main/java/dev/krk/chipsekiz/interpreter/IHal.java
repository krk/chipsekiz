package dev.krk.chipsekiz.interpreter;

import java.util.Optional;

public interface IHal {
    byte getRand();

    void clearScreen();

    boolean draw(byte x, byte y, boolean value);

    void sound(boolean active);

    Optional<Byte> getKey();

    short getCharacterAddress(byte character);
}
