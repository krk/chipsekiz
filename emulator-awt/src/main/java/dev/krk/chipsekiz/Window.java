package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.IDebugger;
import dev.krk.chipsekiz.interpreter.IHal;
import dev.krk.chipsekiz.interpreter.Interpreter;
import dev.krk.chipsekiz.interpreter.InterpreterFactory;
import dev.krk.chipsekiz.sprites.CharacterSprites;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Window extends JFrame {

    Window() {
        super("chipsekiz emulator");

        boolean debuggerEnabled = true;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(64 * 12, 32 * 12 + 68);
        setResizable(false);
        setVisible(true);

        EmulatorCanvas canvas = new EmulatorCanvas(64, 32, 12, 12, Color.WHITE, Color.BLACK);
        add(canvas);

        Tone tone = null;
        try {
            tone = new Tone(1600);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        IDebugger debugger = null;
        if (debuggerEnabled) {
            debugger = new Debugger();
        }

        Hal hal = new Hal(canvas, tone);
        Interpreter interpreter = createInterpreter(hal, debugger);
        Emulator emulator = new Emulator(interpreter);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                emulator.stop();
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                setTitle(
                    String.format("chipsekiz emulator - %d Hz", emulator.getActualFrequency()));
            }
        }, 0, 100);

        emulator.run();

        if (tone != null) {
            tone.close();
        }
    }

    private Interpreter createInterpreter(IHal hal, IDebugger debugger) {
        byte[] program;
        try {
            program = getClass().getClassLoader().getResourceAsStream("demo/chipsekiz-demo.ch8")
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
