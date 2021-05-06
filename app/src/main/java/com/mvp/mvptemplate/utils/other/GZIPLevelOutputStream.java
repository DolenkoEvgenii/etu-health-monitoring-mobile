package com.mvp.mvptemplate.utils.other;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPLevelOutputStream extends GZIPOutputStream {
    public GZIPLevelOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    public void setLevel(int level) {
        def.setLevel(level);
    }
}