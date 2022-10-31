package com.demo.osiguranje.filters;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper 
{
    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private CachedBodyServletOutputStream cachedBody;

    public CachedBodyHttpServletResponse(HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            cachedBody = new CachedBodyServletOutputStream(outputStream);
        }

        return cachedBody;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            cachedBody = new CachedBodyServletOutputStream(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(cachedBody, getResponse().getCharacterEncoding()), true);
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            cachedBody.flush();
        }
    }

    public byte[] getContentAsByteArray() {
        if (cachedBody != null) {
            return cachedBody.getCopy();
        } else {
            return new byte[0];
        }
    }
}