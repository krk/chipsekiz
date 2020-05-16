package dev.krk.chipsekiz;

import dev.krk.chipsekiz.interpreter.Interpreter;
import dev.krk.chipsekiz.interpreter.InterpreterFactory;
import dev.krk.chipsekiz.sprites.CharacterSprites;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Window extends JFrame {
    private boolean isClosing;

    Window() throws IOException {
        super("chipsekiz emulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        isClosing = false;
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                isClosing = true;
            }
        });

        EmulatorCanvas canvas = new EmulatorCanvas(64, 32, 12, 12, Color.WHITE, Color.BLACK);
        add(canvas);
        setSize(64 * 12, 32 * 12 + 68);
        setResizable(false);
        setVisible(true);

        Tone tone = null;
        try {
            tone = new Tone(1600);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        Hal hal = new Hal(canvas, tone);

        byte[] program =
            getClass().getClassLoader().getResourceAsStream("roms/BC_test.ch8").readAllBytes();

        Interpreter chipsekiz =
            InterpreterFactory.create(hal, CharacterSprites.getAddressLocator(), null);

        chipsekiz.load(0x200, program);

        final int[] cycles = {0};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                int c = cycles[0];
                cycles[0] = 0;
                setTitle(String.format("chipsekiz emulator - %d fps", c));
            }
        }, 0, 1000);

        // ~60 ticks per second
        final int BudgetMs = 1000 / 60;

        while (!isClosing) {
            double before = System.currentTimeMillis();
            chipsekiz.tick();
            double duration = System.currentTimeMillis() - before;

            long timeLeft = (long) (BudgetMs - duration);
            if (timeLeft > 0) {
                try {
                    Thread.sleep(timeLeft);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cycles[0]++;
        }

        tone.close();
    }
}
