package dev.krk.emulator;

public enum Register {
    V0(0), V1(1), V2(2), V3(3), V4(4), V5(5), V6(6), V7(7), V8(8), V9(9), V10(10), V11(11), V12(
        12), V13(13), V14(14), V15(15), I(true);

    private int i;
    private boolean isAddress;

    Register(int i) {
        this.i = i;
        this.isAddress = false;
    }

    Register(boolean isAddress) {
        this.i = 0;
        this.isAddress = isAddress;
    }

    public int getIndex() {return i;}

    public boolean isAddress() {return isAddress;}
}
