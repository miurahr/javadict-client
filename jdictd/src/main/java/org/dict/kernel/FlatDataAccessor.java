package org.dict.kernel;

import java.io.IOException;

public class FlatDataAccessor implements IDataAccessor {
    String fDatafile;

    /**
     * FlatDatabase constructor comment.
     */
    public FlatDataAccessor(String fileName) {
        super();
        fDatafile = fileName;
    }

    public String getDatafile() {
        return fDatafile;
    }

    /**
     * readData method comment.
     */
    public byte[] readData(long pos, long len) throws IOException {
        java.io.RandomAccessFile f = null;
        byte[] buf = new byte[(int) len];
        try {
            f = new java.io.RandomAccessFile(getDatafile(), "r");
            f.seek(pos);
            f.readFully(buf);
            return buf;
        } catch (IOException e) {
            throw e;
        } finally {
            if (f != null) {
                f.close();
                f = null;
            }
        }
    }
}
