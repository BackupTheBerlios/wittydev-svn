package org.wittydev.bubble.servlet;
import java.io.IOException;
import java.io.BufferedInputStream;

import javax.servlet.ServletInputStream;

/**
 * Title:        
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class EnsureAvailableMethodInServletInputStream extends ServletInputStream {

    ServletInputStream wrapped;
    BufferedInputStream bis;
    byte[] b=new byte[4096];

    public int readLine(byte[] b, int off, int len) throws IOException {
        byte[] b0=new byte[len];
        int ln= read(b0);
        mark( len+1);
        if ( ln <=0)return ln;
        for (int i=0; i<ln; i++){
            if (b0[i]=='\n'){
                reset();
                skip(i);
                System.arraycopy( b0, 0, b, off, i-1 );
                return i-1;
            }
        }


        return 0;
    }
    public EnsureAvailableMethodInServletInputStream( ServletInputStream sis ){
        this.wrapped=sis;
        bis=new BufferedInputStream (wrapped);
    }
    public int read() throws IOException{
        return bis.read();
    }
    public int read(byte b[]) throws IOException {
        return bis.read(b);
    }
    public int read(byte b[], int off, int len) throws IOException {
        return bis.read(b, off, len);
    }
    public long skip(long n) throws IOException {
        return bis.skip(n);
    }

    public void close() throws IOException {
        bis.close();
    }
    public synchronized void mark(int readlimit) {
        bis.mark(readlimit );
    }
    public synchronized void reset() throws IOException {
        bis.reset();
    }

    public int available() throws IOException {
        mark( b.length+1 );
        int len = read(b);
        reset();
        if (len <0 )
            return 0;
        else
            return len;
    }
}