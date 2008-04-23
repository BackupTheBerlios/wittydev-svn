package org.wittydev.bubble.servlet.http;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class BubbleHttpServletResponse extends HttpServletResponseWrapper {
    WebArchitect webArchitect;
    //ServletOutputStream sos;
    protected BubbleHttpServletResponse(){
        //super(new org.nfj.test.servlet.DummyHttpServletResponse());
        this(null, null);
    }
    public BubbleHttpServletResponse(HttpServletResponse originalResponse,  WebArchitect webArchitect) {
        super(originalResponse);
        this.webArchitect=webArchitect;

        //System.out.println("================ response info ");

    }

    /*public void setBufferSize(int buffer) {
        //System.out.println(getResponse()+"===========================>settIngBufferize: "+buffer );
        getResponse().setBufferSize( buffer );
        //System.out.println(getResponse()+"===========================>gettIngBufferize: "+getResponse().getBufferSize()  );
    }
    public void flushBuffer() throws IOException {
        System.out.println(super.getResponse()+"===========================>flushBufferflushBufferflushBuffer"+getBufferSize() );
        System.out.println("===========================>flushBufferflushBufferflushBuffer");
        getResponse().flushBuffer();
        System.out.println("===========================>flushBufferflushBufferflushBuffer"+super.getBufferSize() );
        System.out.println("===========================>flushBufferflushBufferflushBuffer"+super.isCommitted() );
        System.out.println("===========================>flushBufferflushBufferflushBuffer"+super.getContentType() );




    }

    public ServletOutputStream getOutputStream() throws java.io.IOException {
        if (sos==null)sos=new SOS(super.getOutputStream());
        return sos;
    }*/

    /*class SOS extends ServletOutputStream {
        ServletOutputStream wrapped;
        public SOS( ServletOutputStream wrapped){
            this.wrapped=wrapped;
        }        public void write(int b) throws IOException{
            wrapped.write(b);
        }
        public void write(byte b[]) throws IOException {
            wrapped.write(b);
        }
        public void write(byte b[], int off, int len) throws IOException {
            wrapped.write(b, off, len);
        }
        public void flush() throws IOException {
            //System.out.println("===========================>FLUUUUUUUUUUUUUUUUUSHING");
            wrapped.flush();

            BubbleHttpServletResponse.this.flushBuffer();
        }
        public void close() throws IOException {
            wrapped.close();
        }


        public void print(String p0) throws IOException {
            wrapped.print(p0);
        }
        public void print(boolean p0) throws IOException {
            wrapped.print(p0);
        }
        public void print(char p0) throws IOException {
            wrapped.print(p0);
        }
        public void print(int p0) throws IOException {
            wrapped.print(p0);
        }
        public void print(long p0) throws IOException {
            wrapped.print(p0);
        }
        public void print(float p0) throws IOException {
            wrapped.print(p0);
        }
        public void print(double p0) throws IOException {
            wrapped.print(p0);
        }
        public void println() throws IOException {
            wrapped.println();
        }
        public void println(String p0) throws IOException {
            wrapped.println(p0);
        }
        public void println(boolean p0) throws IOException {
            wrapped.println(p0);
        }
        public void println(char p0) throws IOException {
            wrapped.println(p0);
        }
        public void println(int p0) throws IOException {
            wrapped.println(p0);
        }
        public void println(long p0) throws IOException {
            wrapped.println(p0);
        }
        public void println(float p0) throws IOException {
            wrapped.println(p0);
        }
        public void println(double p0) throws IOException {
            wrapped.println(p0);
        }

    }*/
}