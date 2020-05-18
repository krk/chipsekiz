package dev.krk.chipsekiz.sprites;

import dev.krk.chipsekiz.hal.ICharacterAddressLocator;
import dev.krk.chipsekiz.loader.Layout;
import dev.krk.chipsekiz.loader.Section;

import java.util.Collections;

public class CharacterSprites {
    private static final ICharacterAddressLocator locator =
        character -> (short) ((character & 0xF) * 5);

    // http://www.cs.columbia.edu/~sedwards/classes/2016/4840-spring/designs/Chip8.pdf
    private static final byte[] buffer =
        {(byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xF0,  // 0
            (byte) 0x20, (byte) 0x60, (byte) 0x20, (byte) 0x20, (byte) 0x70,  // 1
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0,  // 2
            (byte) 0xF0, (byte) 0x10, (byte) 0xF0, (byte) 0x10, (byte) 0xF0,  // 3
            (byte) 0x90, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0x10,  // 4
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x10, (byte) 0xF0,  // 5
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0,  // 6
            (byte) 0xF0, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x40,  // 7
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0,  // 8
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x10, (byte) 0xF0,  // 9
            (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90,  // A
            (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0,  // B
            (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0,  // C
            (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0,  // D
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0,  // E
            (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80  // F
        };

    public static byte[] get() {
        return buffer.clone();
    }

    public static ICharacterAddressLocator getAddressLocator() {
        return locator;
    }

    public static Layout DefaultLayout() {
        return new Layout(Collections.singletonList(new Section(0, get())));
    }
}
