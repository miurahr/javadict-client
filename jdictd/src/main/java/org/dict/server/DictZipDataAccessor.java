package org.dict.server;

import org.dict.kernel.IDataAccessor;
import org.dict.zip.DictZipInputStream;
import org.dict.zip.RandomAccessInputStream;

import java.io.IOException;


public class DictZipDataAccessor implements IDataAccessor {
    String fDatafile;

    public DictZipDataAccessor(String fileName) {
        fDatafile = fileName;
    }

    public String getDatafile() {
        return fDatafile;
    }

    private byte[] readData(int start, int len) {
        String s = getDatafile();
        byte[] b = new byte[len];
        try (RandomAccessInputStream in = new RandomAccessInputStream(s, "r");
             DictZipInputStream din = new DictZipInputStream(in)) {
            din.seek(start);
            din.readFully(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public byte[] readData(long pos, long len) {
        return readData((int) pos, (int) len);
    }
}
