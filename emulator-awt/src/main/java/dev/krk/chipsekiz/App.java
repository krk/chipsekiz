package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.Interpreter;
import dev.krk.chipsekiz.interpreter.InterpreterFactory;
import dev.krk.chipsekiz.sprites.CharacterSprites;

import javax.sound.sampled.LineUnavailableException;

import java.awt.Color;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws LineUnavailableException {
        EmulatorOptions options = new EmulatorOptions(true);
        runEmulator(options);
    }

    private static void runEmulator(EmulatorOptions options) throws LineUnavailableException {
        final Tone tone = new Tone(1600);

        EmulatorCanvas canvas = new EmulatorCanvas(64, 32, 12, 12, Color.WHITE, Color.BLACK);
        Hal hal = new Hal(canvas, tone);
        IDebugger debugger = null;
        if (options.isDebuggerEnabled()) {
            debugger = new Debugger();
        }

        byte[] program;
        try {
            // Load demo ROM at start.
            program = App.class.getClassLoader().getResourceAsStream("demo/chipsekiz-demo.ch8")
                .readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Interpreter interpreter =
            InterpreterFactory.create(hal, CharacterSprites.getAddressLocator(), null, debugger);
        final int origin = 0x200;
        interpreter.load(origin, program);

        Emulator emulator = new Emulator(interpreter);
        IEmulatorController emulatorController =
            new EmulatorController(emulator, interpreter, debugger, tone);
        emulatorController.setLoadedProgram(origin, program);
        EmulatorWindow win = new EmulatorWindow(canvas, emulatorController);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                win.setTitle(
                    String.format("chipsekiz emulator - %d Hz", emulator.getActualFrequency()));
            }
        }, 0, 100);

        emulator.run();

        if (tone != null) {
            tone.close();
        }
    }
}
