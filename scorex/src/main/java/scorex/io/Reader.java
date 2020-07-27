package scorex.io;

public abstract class Reader {

    protected Reader() {}

    public int read() throws IOException {
        char[] ca = new char[1];
        if (read(ca, 0, 1) == -1) {
            return -1;
        } else {
            return ca[0];
        }
    }

    public int read(char[] ca) throws IOException {
        return read(ca, 0, ca.length);
    }

    public abstract int read(char[] ca, int off, int len) throws IOException;

    public long skip(long n) throws IOException {
        throw new IOException("skip() not supported");
    }

    public boolean ready() throws IOException {
        return false;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readAheadLimit) throws IOException {
        throw new IOException("mark() not supported");
    }

    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    public abstract void close() throws IOException;
}
