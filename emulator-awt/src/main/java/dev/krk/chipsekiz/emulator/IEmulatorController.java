package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IInterpreter;

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

    void setInterpreter(IInterpreter interpreter, IDebugger debugger);
}
