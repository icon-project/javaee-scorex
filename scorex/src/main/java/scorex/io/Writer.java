package scorex.io;

public abstract class Writer {
    private static final int WRITE_BUFFER_SIZE = 1024;
    private char[] writeBuffer;

    protected Writer() {}

    public void write(int c) throws IOException {
        if (writeBuffer == null) {
            writeBuffer = new char[WRITE_BUFFER_SIZE];
        }
        writeBuffer[0] = (char) c;
        write(writeBuffer, 0, 1);
    }

    public void write(char[] ca) throws IOException {
        write(ca, 0, ca.length);
    }

    public abstract void write(char[] ca, int off, int len) throws IOException;

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(String str, int off, int len) throws IOException {
        char[] ca;
        if (len <= WRITE_BUFFER_SIZE) {
            if (writeBuffer == null) {
                writeBuffer = new char[WRITE_BUFFER_SIZE];
            }
            ca = writeBuffer;
        } else {
            ca = new char[len];
        }
        str.getChars(off, (off + len), ca, 0);
        write(ca, 0, len);
    }

    public abstract void flush() throws IOException;

    public abstract void close() throws IOException;
}
