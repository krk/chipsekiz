package dev.krk.emulator.chipsekiz.loader;

public class Section {
    private int start;
    private byte[] data;

    public Section(int start, byte[] data) {
        this.start = start;
        this.data = data;
    }

    public int start() {
        return start;
    }

    public byte[] data() {
        return data;
    }
}
