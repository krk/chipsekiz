package dev.krk.chipsekiz.interpreter;

import java.util.Optional;

public interface IHal extends IScreenHal {
    byte getRand();

    void sound(boolean active);

    Optional<Byte> getKey();
}
