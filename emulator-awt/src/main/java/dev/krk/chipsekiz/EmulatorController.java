package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.Interpreter;

import javax.sound.sampled.LineUnavailableException;

public class EmulatorController implements IEmulatorController {
    private final Emulator emulator;
    private final Interpreter interpreter;
    private final IDebugger debugger;
    private final Tone tone;
    private int lastLoadedOrigin;
    private byte[] lastLoadedProgram;

    public EmulatorController(Emulator emulator, Interpreter interpreter, IDebugger debugger,
        Tone tone) {
        this.emulator = emulator;
        this.interpreter = interpreter;
        this.debugger = debugger;
        this.tone = tone;
    }

    @Override public void setLoadedProgram(int origin, byte[] program) {
        lastLoadedOrigin = origin;
        lastLoadedProgram = program;
    }

    @Override public void setFrequency(int frequency) {
        emulator.setFrequency(frequency);
    }

    @Override public void setToneFrequency(int frequency) {
        if (frequency == 0) {
            tone.mute();
        } else {
            tone.unmute();
            try {
                tone.setFrequency(frequency);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override public void reset() {
        emulator.pause();
        if (debugger != null) {
            debugger.closeDebuggerWindow();
        }
        interpreter.load(lastLoadedOrigin, lastLoadedProgram);
        emulator.resume();
    }
}
