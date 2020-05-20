package dev.krk.chipsekiz;

import javax.sound.sampled.LineUnavailableException;

public class EmulatorController implements IEmulatorController {
    private final Emulator emulator;
    private final Tone tone;

    public EmulatorController(Emulator emulator, Tone tone) {
        this.emulator = emulator;
        this.tone = tone;
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
}
