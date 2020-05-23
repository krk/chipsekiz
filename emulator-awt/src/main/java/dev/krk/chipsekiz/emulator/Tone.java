package dev.krk.chipsekiz.emulator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

import java.nio.ByteBuffer;

public class Tone {
    public static final int SAMPLE_RATE = 8000;
    private Clip clip;

    private static final int ClipLenMs = 10000;
    private boolean muted;

    Tone(int frequency) throws LineUnavailableException {
        setFrequency(frequency);
    }

    public void setFrequency(int frequency) throws LineUnavailableException {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }

        AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
        ByteBuffer buf = ByteBuffer.allocate(ClipLenMs * SAMPLE_RATE / 1000);
        for (int i = 0; i < ClipLenMs * SAMPLE_RATE / 1000; i++) {
            buf.put((byte) (0.1 * 127 * Math
                .sin(2 * Math.PI * i / ((double) SAMPLE_RATE / frequency))));
        }

        clip = (Clip) AudioSystem.getLine(info);
        clip.open(clip.getFormat(), buf.array(), 0, buf.position());
    }

    public void start() {
        if (muted) {
            return;
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void close() {
        clip.close();
    }

    public void mute() {
        muted = true;
        stop();
    }

    public void unmute() {
        muted = false;
    }
}
