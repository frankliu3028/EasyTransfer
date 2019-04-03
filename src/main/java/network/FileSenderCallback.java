package network;

public interface FileSenderCallback {
    void currentProgress(int progress);
    void finish();
}
