package com.odin.orchestrator.appmgmt.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

class CustomServletInputStream extends ServletInputStream {
    private final ByteArrayInputStream inputStream;

    public CustomServletInputStream(byte[] data) {
        this.inputStream = new ByteArrayInputStream(data);
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public boolean isFinished() {
        return true; // Always return true
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        // No-op for Servlet 3.0 compatibility
    }
}
