package dev.krk.chipsekiz;

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

        Hal hal = new Hal(canvas, tone);
        Emulator emulator = new Emulator(hal);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                emulator.stop();
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                setTitle(String.format("chipsekiz emulator - %d Hz", emulator.getCpuFrequency()));
            }
        }, 0, 100);

        emulator.run();

        tone.close();
    }
}
