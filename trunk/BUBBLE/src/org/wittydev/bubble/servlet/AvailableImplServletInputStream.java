package org.wittydev.bubble.servlet;
import javax.servlet.ServletInputStream;
import java.io.InputStream;
import java.io.IOException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class AvailableImplServletInputStream extends ServletInputStream{
    InputStream wrappedServletInputStream;
    byte[] content;
    int contentLength;
    int alreadyRead, inBuffer;
    boolean eof;

    protected AvailableImplServletInputStream(InputStream wrappedServletInputStream, int contentLength) {

        //System.out.println("===========contentLength==============>"+contentLength);
        this.wrappedServletInputStream=wrappedServletInputStream;
        this.contentLength=contentLength;

        if ( contentLength>0 ) content=new byte[contentLength];
    }

    public AvailableImplServletInputStream(ServletInputStream wrappedServletInputStream, int contentLength) {

        this.wrappedServletInputStream=wrappedServletInputStream;
        this.contentLength=contentLength;

        if ( contentLength>0 )
            content=new byte[contentLength];
        else
            content=new byte[10000];
    }

    public InputStream getInputStream(){
        //System.out.println("getInputStream =========================>?");
        return wrappedServletInputStream;
    }


    public int readLine(byte[] b, int off, int len) throws IOException {
        //System.out.println("readLine =========================>?");
        //int len= wrappedServletInputStream.readLine(b, off, len );
            return read(b, off, len, true);
    }

    public int read() throws IOException {
        //System.out.println("REAAAAADONE =========================>?" );
        if ( wrappedServletInputStream.markSupported() ) return read();
        int val;
        if( alreadyRead==contentLength ) return -1;
        if ( inBuffer==0 )
            val=wrappedServletInputStream.read();
        else{
            val=content[alreadyRead];
            inBuffer--;
        }

        if ( val>=0 ){
            content[alreadyRead]=(byte)val;
            alreadyRead+=1;
        }
        //System.out.println("REAAAAADONE ["+val+"]=========================>"+val );
        return val;
    }

    public int read(byte b[]) throws IOException {
        //System.out.println("read bytes=========================>?");
        return read( b, 0, b.length );
    }

    public int read(byte b[], int off, int len) throws IOException {
        //System.out.println("read bytes 2 =========================>?");
        return read ( b, off, len, false );
    }
    protected int read(byte b[], int off, int len, boolean readLineOnly) throws IOException {
        if ( wrappedServletInputStream .markSupported() ) {
            if (readLineOnly )
                return wrappedServletInputStream .read(b, off, len);
            else
                return ((ServletInputStream)wrappedServletInputStream) .readLine( b, off, len);
        }

        //System.out.println("REAAAAAD [rlo:"+readLineOnly+"]BBBBBBBBBBB=========================>?" );
        int ln;//= wrappedServletInputStream.read(b, off, len);
        if( alreadyRead==contentLength ) return -1;
        if (inBuffer<=0 && eof) return -1;
        if ( alreadyRead+len >contentLength )
            len=contentLength-alreadyRead;


        if ( this.inBuffer ==0)
            ln = wrappedServletInputStream.read(content, alreadyRead, len);
        else{
            if ( inBuffer>=len ){
                ln=len;
                inBuffer-=len;
            }else {
                ln = wrappedServletInputStream.read(content, alreadyRead+inBuffer , len );
                if ( ln<0 ){
                    eof=true;
                    ln = len-inBuffer;
                }else
                    ln+=len-inBuffer ;
                inBuffer=0;
            }
        }
        if ( ln>0 ){
            int skipEOL=0;
            if ( readLineOnly ){

                int i=0;
                for ( ; i<ln; i++){
                    if ( content[alreadyRead+i] ==  '\n' )break;
                }

                if ( i<ln){
                    //System.out.println("aaaaa"+i);
                    inBuffer+=ln-i+1;
                    ln=i;
                    skipEOL=1;

                }
            }

            System.arraycopy( content, alreadyRead, b, off, ln);
            alreadyRead+=ln+skipEOL;
        }
        //System.out.println("REAAAAAD ["+ln+"]=========================>"+new String(b) );
        return ln;
    }


    public long skip(long n) throws IOException {
        //System.out.println("skip ========================>?" );
        if ( wrappedServletInputStream .markSupported() ) return wrappedServletInputStream .skip(n);

        if( alreadyRead==contentLength ) return -1;
        int len=read(content, alreadyRead, (int)n);
        return len;
    }
    public int available() throws IOException {
        System.out.println("available ========================>?" );
        if ( wrappedServletInputStream .markSupported() ) {
            wrappedServletInputStream .mark(content.length);
            int len= wrappedServletInputStream .read(content);
            wrappedServletInputStream .reset();
            System.out.println("available ========================AAAAAAAAAA>"+len );
            return len;
        }

        System.out.println("available ========================AAAAAAAAAA>" );
        if( alreadyRead==contentLength ) return -1;
        if( inBuffer>0) return inBuffer;
        if (inBuffer<=0 && eof) return -1;

        int len=wrappedServletInputStream.read(content, alreadyRead, (contentLength-alreadyRead));
        if (len >0 )inBuffer=len;
        /*else if ( len<0 ){
            eof=true;
        }*/
        //System.out.println("available =========================>"+len );
        return len;
    }
    public void close() throws IOException {
        wrappedServletInputStream.close();
    }
    private int markPos=-1;
    public synchronized void mark(int readlimit) {
        if (wrappedServletInputStream .markSupported() )
            wrappedServletInputStream .mark(readlimit+1 );
        else{
            markPos=alreadyRead;
        }
    }
    public synchronized void reset() throws IOException {
        if (wrappedServletInputStream .markSupported() )
            wrappedServletInputStream .reset();
        else{
            if ( markPos>=0 ) {
                inBuffer+=(alreadyRead-markPos);
                alreadyRead=markPos;
            }
            markPos=-1;
        }
    }
    public boolean markSupported() {
        return true;
    }



    public static void main(String[] args) throws Exception {
        //byte[] b="AAAOui Je suis\n Catherine De\n Neuve!O".getBytes();
        //byte[] b="AAA".getBytes();

        java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream os=new java.io.ObjectOutputStream(baos);
        os.writeObject(new Long(5));
        os.writeObject("Carlos");
        os.flush();
        byte[] b=baos.toByteArray();
        java.io.ByteArrayInputStream bis=new java.io.ByteArrayInputStream (b );

        AvailableImplServletInputStream bsis=new AvailableImplServletInputStream (bis, b.length );

        java.io.ObjectInputStream ois=new java.io.ObjectInputStream (bsis);
        System.out.println( ois.readObject() );
        System.out.println( ois.readObject() );


    }/**/

        /*byte[] b="0123\n456\n7890123456789012\n".getBytes();
        //byte[] b="".getBytes();


        AvailableImplServletInputStream bsis=new AvailableImplServletInputStream (bis, b.length );
        byte[] buff=new byte[5];
        int len;
        len=bsis.read(buff);
        System.out.println(len );
        //System.out.println(len+">>>>>>"+ new  String( buff, 0, len  ) );
        while (bsis.available()>0){
            //len=bsis.read(buff);
            len=bsis.readLine(buff, 0, buff.length );
            System.out.println(len+">>>"+ new  String( buff, 0, len  ) );
        }
        System.out.println(">>>"+ bsis.available() );
        System.out.println(">>>"+ bsis.available() );

        //System.out.println(">>>"+ bsis.available() );
        //System.out.println(">>>"+ (char)bsis.read() );
        //System.out.println(">>>"+ bsis.available() );

        //while (bsis.available()>0){
        //    System.out.println(">>>"+ (char)bsis.read() );
        //}
    }/**/

    /*
    //// ok funziona
    public int read(byte b[], int off, int len) throws IOException {
        int ln;//= wrappedServletInputStream.read(b, off, len);
        if( alreadyRead==contentLength ) return -1;
        if (inBuffer<=0 && eof) return -1;
        if ( alreadyRead+len >contentLength )
            len=contentLength-alreadyRead;


        if ( this.inBuffer ==0)
            ln = wrappedServletInputStream.read(content, alreadyRead, len);
        else{
            if ( inBuffer>=len ){
                ln=len;
                inBuffer-=len;
            }else {
                ln = wrappedServletInputStream.read(content, alreadyRead+inBuffer , len );
                if ( ln<0 ){
                    eof=true;
                    ln = len-inBuffer;
                }else
                    ln+=len-inBuffer ;
                inBuffer=0;
            }
        }
        if ( ln>0 ){
            System.arraycopy( content, alreadyRead, b, off, ln);
            alreadyRead+=ln;
        }
        return ln;
    }*/

}