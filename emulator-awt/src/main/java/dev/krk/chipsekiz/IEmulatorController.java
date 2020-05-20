package dev.krk.chipsekiz;

public interface IEmulatorController {
    void load(int origin, byte[] program);

    void setLoadedProgram(int origin, byte[] program);

    void setFrequency(int frequency);

    void setToneFrequency(int frequency);

    void reset();

    void keyUp();

    void keyDown(char keyChar);

    void pause();

    void resume();
}
