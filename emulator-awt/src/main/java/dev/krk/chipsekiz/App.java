package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.Interpreter;
import dev.krk.chipsekiz.interpreter.InterpreterFactory;
import dev.krk.chipsekiz.sprites.CharacterSprites;

import javax.sound.sampled.LineUnavailableException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        EmulatorWindow win = new EmulatorWindow();

        Tone tone = null;
        try {
            tone = new Tone(1600);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        Hal hal = new Hal(win.getScreenHal(), tone);
        Emulator emulator = createEmulator(hal, new EmulatorOptions(true));

        emulator.run();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                win.setTitle(
                    String.format("chipsekiz emulator - %d Hz", emulator.getActualFrequency()));
            }
        }, 0, 100);

        if (tone != null) {
            tone.close();
        }
    }

    private static Emulator createEmulator(IHal hal, EmulatorOptions options) {
        IDebugger debugger = null;
        if (options.isDebuggerEnabled()) {
            debugger = new Debugger();
        }

        Interpreter interpreter = createInterpreter(hal, debugger);
        return new Emulator(interpreter);
    }

    private static Interpreter createInterpreter(IHal hal, IDebugger debugger) {
        byte[] program;
        try {
            // Load demo ROM at start.
            program = App.class.getClassLoader().getResourceAsStream("demo/chipsekiz-demo.ch8")
                .readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Interpreter chipsekiz =
            InterpreterFactory.create(hal, CharacterSprites.getAddressLocator(), null, debugger);
        chipsekiz.load(0x200, program);
        return chipsekiz;
    }
}
