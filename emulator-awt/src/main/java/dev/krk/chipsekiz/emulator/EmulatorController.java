package dev.krk.chipsekiz.emulator;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IInterpreter;

import javax.sound.sampled.LineUnavailableException;

public class EmulatorController implements IEmulatorController {
    private final Emulator emulator;
    private IInterpreter interpreter;
    private final IDebugger debugger;
    private final Hal hal;
    private final Tone tone;
    private int lastLoadedOrigin;
    private byte[] lastLoadedProgram;

    public EmulatorController(Emulator emulator, IInterpreter interpreter, IDebugger debugger,
        Hal hal, Tone tone) {
        this.emulator = emulator;
        this.interpreter = interpreter;
        this.debugger = debugger;
        this.hal = hal;
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

    @Override public void load(int origin, byte[] program) {
        setLoadedProgram(origin, program);
        reset();
    }

    @Override public void reset() {
        emulator.pause();
        if (debugger != null) {
            debugger.closeDebuggerWindow();
        }
        interpreter.load(lastLoadedOrigin, lastLoadedProgram);
        emulator.resume();
    }

    @Override public void keyUp() {
        hal.keyUp();
    }

    @Override public void keyDown(char keyChar) {
        char key = Character.toLowerCase(keyChar);
        if (key >= 'a' && key <= 'f') {
            hal.keyDown((byte) (key - 'a' + 0xA));
        } else if (key >= '0' && key <= '9') {
            hal.keyDown((byte) (key - '0'));
        }
    }

    @Override public void stop() {
        emulator.stop();
        if (debugger != null) {
            debugger.closeDebuggerWindow();
        }
    }

    @Override public void pause() {
        emulator.pause();
    }

    @Override public void resume() {
        emulator.resume();
    }

    @Override public void setInterpreter(IInterpreter interpreter, IDebugger debugger) {
        emulator.pause();
        interpreter.setDebugger(debugger);
        emulator.setInterpreter(interpreter);
        this.interpreter = interpreter;
        emulator.resume();
    }

    @Override public int getActualFrequency() {
        return emulator.getActualFrequency();
    }

    @Override public void run() {
        emulator.run();
    }
}
