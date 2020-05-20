package dev.krk.chipsekiz;

public interface IEmulatorController {
    void setLoadedProgram(int origin, byte[] program);

    void setFrequency(int frequency);

    void setToneFrequency(int frequency);

    void reset();
}
