package dev.krk.chipsekiz.emulator;

public interface IEmulatableFactory {
    IEmulatable create(EmulatorOptions options, Tone tone);
}
